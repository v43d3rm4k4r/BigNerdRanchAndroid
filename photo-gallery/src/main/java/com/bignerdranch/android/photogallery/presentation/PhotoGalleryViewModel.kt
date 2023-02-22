package com.bignerdranch.android.photogallery.presentation

import androidx.lifecycle.ViewModel
import com.bignerdranch.android.photogallery.domain.FlickrFetcher
import com.bignerdranch.android.photogallery.domain.model.GalleryItem
import com.bignerdranch.android.photogallery.ui.recyclerviewutils.PhotoAdapter

class PhotoGalleryViewModel : ViewModel() {

    private val flickrFetcher = FlickrFetcher()
    val galleryItemsLiveData = flickrFetcher.fetchPhotos()

    val thumbnailDownloader = ThumbnailDownloader<PhotoAdapter.PhotoHolder>()

    fun onPhotoClicked(galleryItem: GalleryItem) {
        TODO()
    }

    override fun onCleared() {
        super.onCleared()
        flickrFetcher.cancelRequestInFlight()
    }
}