package com.bignerdranch.android.photogallery.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.WorkerThread
import com.bignerdranch.android.androidutils.livedata.SingleLiveEvent
import com.bignerdranch.android.photogallery.data.flickrapi.FlickrResponseJSON
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface FlickrFetcher {

    val events: SingleLiveEvent<FlickrFetcherSingleLiveEvent>

    fun fetchInterestingPhotosRequest(): Call<FlickrResponseJSON>
    fun searchPhotosRequest(query: String): Call<FlickrResponseJSON>

    fun fetchInterestingPhotos():    GalleryItemsLiveData
    fun searchPhotos(query: String): GalleryItemsLiveData

    @WorkerThread
    fun fetchPhoto(url: String): Bitmap?

    fun cancelRequestInFlight()
}