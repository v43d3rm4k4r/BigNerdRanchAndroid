package com.bignerdranch.android.photogallery.di

import android.content.Context
import com.bignerdranch.android.androidutils.fastLazyViewModel
import com.bignerdranch.android.androidutils.network.ConnectivityObserver
import com.bignerdranch.android.androidutils.network.NetworkConnectivityObserver
import com.bignerdranch.android.photogallery.data.FlickrFetcher
import com.bignerdranch.android.photogallery.data.FlickrFetcherImpl
import com.bignerdranch.android.photogallery.data.QueryPreferences
import com.bignerdranch.android.photogallery.data.QueryStore
import com.bignerdranch.android.photogallery.presentation.PhotoGalleryViewModel
import com.bignerdranch.android.photogallery.presentation.PhotoGalleryViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object PresentationModule {

    @Singleton
    fun providePhotoGalleryViewModelFactory(
        queryPreferences: QueryStore,
        networkConnectivityObserver: ConnectivityObserver,
        flickrFetcherImpl: FlickrFetcher
    ): PhotoGalleryViewModelFactory = PhotoGalleryViewModelFactory(queryPreferences,
        networkConnectivityObserver,
        flickrFetcherImpl)
}