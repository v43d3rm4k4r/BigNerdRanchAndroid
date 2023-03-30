package com.bignerdranch.android.photogallery.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.bignerdranch.android.androidutils.livedata.SingleLiveEvent
import com.bignerdranch.android.photogallery.domain.model.GalleryItem
import com.bignerdranch.android.photogallery.data.flickrapi.FlickrAPI
import com.bignerdranch.android.photogallery.data.flickrapi.FlickrResponseJSON
import com.bignerdranch.android.photogallery.data.FlickrFetcherSingleLiveEvent.ErrorLoading

import retrofit2.*
import javax.inject.Inject

typealias GalleryItemsLiveData = LiveData<List<GalleryItem>>

class FlickrFetcherImpl @Inject constructor(
    retrofit: Retrofit
) : FlickrFetcher {

    private val flickrAPI: FlickrAPI = retrofit.create()

    private var flickrRequest: Call<FlickrResponseJSON>? = null

    private val responseLiveData = MutableLiveData<List<GalleryItem>>()

    override val events = SingleLiveEvent<FlickrFetcherSingleLiveEvent>()

    override fun fetchInterestingPhotosRequest():    Call<FlickrResponseJSON> = flickrAPI.fetchInterestingPhotos()
    override fun searchPhotosRequest(query: String): Call<FlickrResponseJSON> = flickrAPI.searchPhotos(query)

    override fun fetchInterestingPhotos():    GalleryItemsLiveData = fetchPhotoMetadata(fetchInterestingPhotosRequest())
    override fun searchPhotos(query: String): GalleryItemsLiveData = fetchPhotoMetadata(searchPhotosRequest(query))

    private fun fetchPhotoMetadata(flickrRequest: Call<FlickrResponseJSON>): GalleryItemsLiveData { // TODO: get other pages + add paging3
        this.flickrRequest = flickrRequest
        flickrRequest.enqueue(object : Callback<FlickrResponseJSON> {

            override fun onResponse(call: Call<FlickrResponseJSON>, response: Response<FlickrResponseJSON>) {
                Log.d(TAG, "Response received: ${response.body()}")
                val flickrResponse = response.body()
                val photosResponse = flickrResponse?.photos ?: return
                var galleryItems = photosResponse.galleryItems ?: mutableListOf()
                galleryItems = galleryItems.filter {
                    it.url.isNotBlank()
                }
                responseLiveData.postValue(galleryItems)
            }

            override fun onFailure(call: Call<FlickrResponseJSON>, t: Throwable) {
                val msg = if (call.isCanceled) "Request has been canceled"
                else "Failed to fetch photos"
                Log.e(TAG, msg, t)
                events.postValue(ErrorLoading)
            }
        })
        return responseLiveData
    }

    @WorkerThread
    override fun fetchPhoto(url: String): Bitmap? {
        val response = flickrAPI.fetchUrlBytes(url).execute()
        val bitmap = response.body()?.byteStream()?.use(BitmapFactory::decodeStream)
        Log.i(TAG, "Decoded bitmap=$bitmap from Response=$response")
        return bitmap
    }

    override fun cancelRequestInFlight() {
        if (!flickrRequest?.isCanceled!!)
            flickrRequest?.cancel()
    }

    private companion object {
        const val TAG = "FlickrFetcher"
    }
}