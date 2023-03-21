package com.bignerdranch.android.photogallery.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.bignerdranch.android.androidutils.SingleLiveEvent
import com.bignerdranch.android.photogallery.domain.model.GalleryItem
import com.bignerdranch.android.photogallery.utils.flickrapi.FlickrAPI
import com.bignerdranch.android.photogallery.utils.flickrapi.FlickrResponse
import com.bignerdranch.android.photogallery.utils.flickrapi.PhotoInterceptor
import com.bignerdranch.android.photogallery.model.FlickrFetcherSingleLiveEvent.ErrorLoading

import okhttp3.OkHttpClient

import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

typealias GalleryItemsLiveData = LiveData<List<GalleryItem>>

class FlickrFetcher {

    private val flickrAPI: FlickrAPI
    private var flickrRequest: Call<FlickrResponse>? = null

    private val responseLiveData = MutableLiveData<List<GalleryItem>>()

    val events = SingleLiveEvent<FlickrFetcherSingleLiveEvent>()

    init {
        val client = OkHttpClient.Builder()
            .addInterceptor(PhotoInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        flickrAPI = retrofit.create()
    }

    fun fetchInterestingPhotos(): GalleryItemsLiveData =
        fetchPhotoMetadata(flickrAPI.fetchInterestingPhotos())

    fun searchPhotos(query: String): GalleryItemsLiveData =
        fetchPhotoMetadata(flickrAPI.searchPhotos(query))

    private fun fetchPhotoMetadata(flickrRequest: Call<FlickrResponse>): GalleryItemsLiveData { // TODO: get other pages + add paging3
        this.flickrRequest = flickrRequest
        flickrRequest.enqueue(object : Callback<FlickrResponse> {

            override fun onResponse(call: Call<FlickrResponse>, response: Response<FlickrResponse>) {
                Log.d(TAG, "Response received: ${response.body()}")
                val flickrResponse = response.body()
                val photosResponse = flickrResponse?.photos ?: return
                var galleryItems = photosResponse.galleryItems ?: mutableListOf()
                galleryItems = galleryItems.filter {
                    it.url.isNotBlank()
                }
                responseLiveData.postValue(galleryItems)
            }

            override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
                val msg = if (call.isCanceled) "Request has been canceled"
                else "Failed to fetch photos"
                Log.e(TAG, msg, t)
                events.postValue(ErrorLoading)
            }
        })
        return responseLiveData
    }

    @WorkerThread
    fun fetchPhoto(url: String): Bitmap? {
        val response = flickrAPI.fetchUrlBytes(url).execute()
        val bitmap = response.body()?.byteStream()?.use(BitmapFactory::decodeStream)
        Log.i(TAG, "Decoded bitmap=$bitmap from Response=$response")
        return bitmap
    }

    fun cancelRequestInFlight() {
        if (!flickrRequest?.isCanceled!!)
            flickrRequest?.cancel()
    }

    private companion object {
        const val TAG = "FlickrFetcher"
    }
}