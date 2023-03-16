package com.bignerdranch.android.photogallery.presentation

import android.app.Application
import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.os.Looper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

import com.bignerdranch.android.androidutils.SingleLiveEvent
import com.bignerdranch.android.photogallery.domain.FlickrFetcher
import com.bignerdranch.android.photogallery.domain.QueryPreferences
import com.bignerdranch.android.photogallery.domain.model.GalleryItem
import com.bignerdranch.android.photogallery.presentation.PhotoGallerySingleLiveEvent.*
import com.bignerdranch.android.photogallery.ui.recyclerviewutils.PhotoAdapter
import com.bignerdranch.android.photogallery.presentation.PhotoGallerySingleLiveEvent.ShowProgressBar

// TODO: add reinitialization after network connection
class PhotoGalleryViewModel(
    private val queryPreferences: QueryPreferences
) : ViewModel() {

    private val flickrFetcher = FlickrFetcher()

    private val _searchTerm = MutableLiveData("")
    val searchTerm: String get() = _searchTerm.value ?: ""

    private val events = SingleLiveEvent<PhotoGallerySingleLiveEvent>()
    private val flickrFetcherEvents = flickrFetcher.events

    val mediator: LiveData<PhotoGallerySingleLiveEvent> = MediatorLiveData<PhotoGallerySingleLiveEvent>().apply {
        addSource(events) { value = it }
        addSource(flickrFetcherEvents) { value = ShowRequestError }
    }

    val galleryItemsLiveData = Transformations.switchMap(_searchTerm) { searchTerm ->
        events.postValue(ShowProgressBar)
        if (searchTerm.isBlank())
            flickrFetcher.fetchInterestingPhotos()
        else
            flickrFetcher.searchPhotos(searchTerm)
    }

    val thumbnailDownloader = ThumbnailDownloader<PhotoAdapter.PhotoHolder>(
        flickrFetcher,
        Handler(Looper.getMainLooper())) { photoHolder, bitmap ->
            val drawable = BitmapDrawable(Resources.getSystem(), bitmap)
            photoHolder.bindDrawable(drawable)
        }

    init {
        thumbnailDownloader.start()
        _searchTerm.value = queryPreferences.getStoredQuery()
    }

    fun searchPhotos(query: String = "") {
        queryPreferences.setStoredQuery(query)
        _searchTerm.postValue(query)
    }

    fun onPhotoClicked(galleryItem: GalleryItem) {
        //TODO()
    }

    override fun onCleared() {
        super.onCleared()
        flickrFetcher.cancelRequestInFlight()
        thumbnailDownloader.quit()
    }
}