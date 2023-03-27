package com.bignerdranch.android.photogallery.data.flickrapi

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Here we add parameters common to all requests.
 */
class PhotoInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newUrl = originalRequest.url().newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .addQueryParameter("format", "json")
            .addQueryParameter("nojsoncallback", "1")
            .addQueryParameter("extras", "url_s")
            .addQueryParameter("safesearch", "1")
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }

    private companion object {
        const val API_KEY = "e362e26e424002430b3ff34146148179"
    }
}