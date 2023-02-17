package com.bignerdranch.android.photogallery.presentation

import androidx.lifecycle.ViewModel
import com.bignerdranch.android.photogallery.domain.FlickrFetcher
import com.bignerdranch.android.photogallery.domain.model.GalleryItem

class PhotoGalleryViewModel : ViewModel() {

    private val flickrFetcher = FlickrFetcher()
    val galleryItemsLiveData = flickrFetcher.fetchPhotos()

    fun onPhotoClicked(galleryItem: GalleryItem) {
        TODO()
    }

    override fun onCleared() {
        super.onCleared()
        flickrFetcher.cancelRequestInFlight()
    }
}