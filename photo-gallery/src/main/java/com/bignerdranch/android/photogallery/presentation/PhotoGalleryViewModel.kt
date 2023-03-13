package com.bignerdranch.android.photogallery.presentation

import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.os.Looper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

import com.bignerdranch.android.photogallery.domain.FlickrFetcher
import com.bignerdranch.android.photogallery.domain.model.GalleryItem
import com.bignerdranch.android.photogallery.ui.recyclerviewutils.PhotoAdapter

class PhotoGalleryViewModel(
    private val resources: Resources
) : ViewModel() {

    private val flickrFetcher = FlickrFetcher()
    private val mutableSearchTerm = MutableLiveData("")

    private var isInitialCall = true

    val galleryItemsLiveData = Transformations.switchMap(mutableSearchTerm) { searchTerm ->
        if (isInitialCall) {
            isInitialCall = false
            flickrFetcher.fetchInterestingPhotos()
        }
        else
            flickrFetcher.searchPhotos(searchTerm)
    }

    val thumbnailDownloader = ThumbnailDownloader<PhotoAdapter.PhotoHolder>(
        flickrFetcher,
        Handler(Looper.getMainLooper())) { photoHolder, bitmap ->
            val drawable = BitmapDrawable(resources, bitmap)
            photoHolder.bindDrawable(drawable)
        }

    init {
        thumbnailDownloader.start()
    }

    fun searchPhotos(query: String = "") {
        mutableSearchTerm.postValue(query)
    }

    fun onPhotoClicked(galleryItem: GalleryItem) {
        TODO()
    }

    override fun onCleared() {
        super.onCleared()
        flickrFetcher.cancelRequestInFlight()
        thumbnailDownloader.quit()
    }
}