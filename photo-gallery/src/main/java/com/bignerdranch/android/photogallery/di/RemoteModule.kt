package com.bignerdranch.android.photogallery.di

import android.content.Context
import com.bignerdranch.android.androidutils.network.ConnectivityObserver
import com.bignerdranch.android.androidutils.network.NetworkConnectivityObserver
import com.bignerdranch.android.photogallery.data.FlickrFetcher
import com.bignerdranch.android.photogallery.data.FlickrFetcherImpl
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
object RemoteModule {

    @Singleton
    @Provides
    fun providePhotoInterceptor(): Interceptor =
        Interceptor { chain ->
            val originalRequest = chain.request()

            val newUrl = originalRequest.url().newBuilder()
                .addQueryParameter("api_key", "e362e26e424002430b3ff34146148179")
                .addQueryParameter("format", "json")
                .addQueryParameter("nojsoncallback", "1")
                .addQueryParameter("extras", "url_s")
                .addQueryParameter("safesearch", "1")
                .build()

            val newRequest = originalRequest.newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    @Singleton
    @Provides
    fun provideNetworkConnectivityObserver(context: Context): ConnectivityObserver = NetworkConnectivityObserver(context)

    @Singleton
    @Provides
    fun provideFlickrFetcher(retrofit: Retrofit): FlickrFetcher = FlickrFetcherImpl(retrofit)
}