package com.bignerdranch.android.photogallery.presentation

import androidx.lifecycle.ViewModel
import com.bignerdranch.android.photogallery.domain.FlickrFetcher

class PhotoGalleryViewModel : ViewModel() {

    val galleryItemLiveData = FlickrFetcher().fetchPhotos()
}