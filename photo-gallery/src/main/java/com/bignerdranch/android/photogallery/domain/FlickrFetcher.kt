package com.bignerdranch.android.photogallery.domain

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.bignerdranch.android.photogallery.domain.model.GalleryItem
import com.bignerdranch.android.photogallery.utils.flickrapi.FlickrAPI
import com.bignerdranch.android.photogallery.utils.flickrapi.FlickrResponse

import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class FlickrFetcher {

    private val flickrAPI: FlickrAPI
    private val flickrHomePageRequest: Call<FlickrResponse>

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        flickrAPI = retrofit.create()
        flickrHomePageRequest = flickrAPI.fetchPhotos()
    }

    fun fetchPhotos(): LiveData<List<GalleryItem>> { // TODO: get other pages + add paging3
        val responseLiveData = MutableLiveData<List<GalleryItem>>()

        flickrHomePageRequest.enqueue(object : Callback<FlickrResponse> {

            override fun onResponse(call: Call<FlickrResponse>, response: Response<FlickrResponse>) {
                Log.d(TAG, "Response received: ${response.body()}")
                val flickrResponse = response.body()
                val photosResponse = flickrResponse?.photos
                var galleryItems = photosResponse?.galleryItems ?: mutableListOf()
                galleryItems = galleryItems.filter {
                    it.url.isNotBlank()
                }
                responseLiveData.postValue(galleryItems)
            }

            override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
                val msg = if (call.isCanceled) "Request has been canceled"
                else "Failed to fetch photos"
                Log.e(TAG, msg, t)
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
        if (!flickrHomePageRequest.isCanceled)
            flickrHomePageRequest.cancel()
    }

    private companion object {
        const val TAG = "FlickrFetcher"
    }
}