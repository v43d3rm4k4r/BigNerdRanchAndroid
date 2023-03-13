package com.bignerdranch.android.photogallery.presentation

import android.app.Application
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.os.Looper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

import com.bignerdranch.android.photogallery.domain.FlickrFetcher
import com.bignerdranch.android.photogallery.domain.QueryPreferences
import com.bignerdranch.android.photogallery.domain.model.GalleryItem
import com.bignerdranch.android.photogallery.ui.recyclerviewutils.PhotoAdapter

class PhotoGalleryViewModel(
    private val app: Application
) : ViewModel() {

    private val flickrFetcher = FlickrFetcher()

    private val _searchTerm = MutableLiveData("")
    val searchTerm: String get() = _searchTerm.value ?: ""

    val galleryItemsLiveData = Transformations.switchMap(_searchTerm) { searchTerm ->
        if (searchTerm.isBlank())
            flickrFetcher.fetchInterestingPhotos()
        else
            flickrFetcher.searchPhotos(searchTerm)
    }

    val thumbnailDownloader = ThumbnailDownloader<PhotoAdapter.PhotoHolder>(
        flickrFetcher,
        Handler(Looper.getMainLooper())) { photoHolder, bitmap ->
            val drawable = BitmapDrawable(app.resources, bitmap)
            photoHolder.bindDrawable(drawable)
        }

    init {
        thumbnailDownloader.start()
        _searchTerm.value = QueryPreferences.getStoredQuery(app)
    }

    fun searchPhotos(query: String = "") {
        QueryPreferences.setStoredQuery(app.applicationContext, query)
        _searchTerm.postValue(query)
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