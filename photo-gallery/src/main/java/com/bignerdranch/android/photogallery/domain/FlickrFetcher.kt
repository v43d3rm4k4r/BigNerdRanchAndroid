package com.bignerdranch.android.photogallery.domain

import android.util.Log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.photogallery.domain.model.GalleryItem

import com.bignerdranch.android.photogallery.utils.flickrapi.FlickrAPI
import com.bignerdranch.android.photogallery.utils.flickrapi.FlickrResponse

import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class FlickrFetcher {

    private val flickrAPI: FlickrAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        flickrAPI = retrofit.create()
    }

    fun fetchPhotos(): LiveData<List<GalleryItem>> {
        val responseLiveData = MutableLiveData<List<GalleryItem>>()
        val flickrHomePageRequest = flickrAPI.fetchPhotos()

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
                Log.e(TAG, "Failed to fetch photos", t)
            }
        })
        return responseLiveData
    }

    private companion object {
        const val TAG = "FlickrFetcher"
    }
}