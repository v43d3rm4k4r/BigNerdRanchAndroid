package com.bignerdranch.android.photogallery.presentation

import com.bignerdranch.android.androidutils.network.ConnectivityObserver
import com.bignerdranch.android.photogallery.domain.model.GalleryItem

sealed class PhotoGallerySingleLiveEvent {

    object ShowProgressBar : PhotoGallerySingleLiveEvent()

    object HideProgressBar : PhotoGallerySingleLiveEvent()

    object ShowRequestError : PhotoGallerySingleLiveEvent()

    class ShowResult(val galleryItems: List<GalleryItem>) : PhotoGallerySingleLiveEvent()

    object ShowEmptyResult : PhotoGallerySingleLiveEvent()

    class ShowNetworkStatus(val status: ConnectivityObserver.Status) : PhotoGallerySingleLiveEvent()
}