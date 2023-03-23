package com.bignerdranch.android.photogallery.presentation

import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.os.Looper

import androidx.lifecycle.*
import com.bignerdranch.android.androidutils.livedata.NotNullMediatorLiveData

import com.bignerdranch.android.androidutils.livedata.SingleLiveEvent
import com.bignerdranch.android.androidutils.livedata.SingleMediatorLiveEvent
import com.bignerdranch.android.androidutils.network.ConnectivityObserver
import com.bignerdranch.android.androidutils.network.ConnectivityObserverSingleLiveEvent
import com.bignerdranch.android.photogallery.data.QueryStore
import com.bignerdranch.android.photogallery.data.FlickrFetcher
import com.bignerdranch.android.photogallery.data.GalleryItemsLiveData
import com.bignerdranch.android.photogallery.domain.model.GalleryItem
import com.bignerdranch.android.photogallery.presentation.PhotoGallerySingleLiveEvent.*
import com.bignerdranch.android.photogallery.ui.recyclerviewutils.PhotoAdapter
import com.bignerdranch.android.photogallery.presentation.PhotoGallerySingleLiveEvent.ShowProgressBar

// TODO: Fix download on turn screen
class PhotoGalleryViewModel(
    val queryStore: QueryStore,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val flickrFetcher = FlickrFetcher()

    private val _searchTerm = MutableLiveData("")
    val searchTerm: String get() = _searchTerm.value ?: ""

    private val events = SingleLiveEvent<PhotoGallerySingleLiveEvent>()

    private var isInitCall = true
    val mediator: LiveData<PhotoGallerySingleLiveEvent> = SingleMediatorLiveEvent<PhotoGallerySingleLiveEvent>().apply {
        addSource(events) { value = it }
        addSource(flickrFetcher.events) { value = ShowRequestError }
        addSource(connectivityObserver.events) {
            if (isInitCall) {
                isInitCall = false
                return@addSource
            }
            val status = (it as ConnectivityObserverSingleLiveEvent.NetworkStatus).status
            if (status == ConnectivityObserver.Status.AVAILABLE) startGettingPhotos()
            value = ShowNetworkStatus(status)
        }
    }

    val galleryItemsLiveData = Transformations.switchMap(_searchTerm) { searchTerm ->
        startGettingPhotos(searchTerm)
    }

    val thumbnailDownloader = ThumbnailDownloader<PhotoAdapter.PhotoHolder>(
        flickrFetcher,
        Handler(Looper.getMainLooper())) { photoHolder, bitmap ->
            val drawable = BitmapDrawable(Resources.getSystem(), bitmap)
            photoHolder.bindDrawable(drawable)
        }

    init {
        thumbnailDownloader.start()
        _searchTerm.value = queryStore.getStoredQuery()
    }

    private fun startGettingPhotos(query: String = ""): GalleryItemsLiveData {
        events.postValue(ShowProgressBar)
        return if (searchTerm.isBlank())
            flickrFetcher.fetchInterestingPhotos()
        else
            flickrFetcher.searchPhotos(query)
    }

    fun searchPhotos(query: String = "") {
        queryStore.setStoredQuery(query)
        _searchTerm.postValue(query)
    }

    fun onPhotoClicked(galleryItem: GalleryItem) {
        //TODO()
    }

    override fun onCleared() {
        super.onCleared()
        flickrFetcher.cancelRequestInFlight()
        thumbnailDownloader.quit()
        connectivityObserver.onClear()
    }
}