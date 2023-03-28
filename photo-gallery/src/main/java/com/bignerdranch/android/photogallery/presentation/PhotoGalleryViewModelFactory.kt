package com.bignerdranch.android.photogallery.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.androidutils.network.ConnectivityObserver
import com.bignerdranch.android.photogallery.data.FlickrFetcher
import com.bignerdranch.android.photogallery.data.QueryStore
import javax.inject.Inject

class PhotoGalleryViewModelFactory @Inject constructor(
    private val queryPreferences: QueryStore,
    private val networkConnectivityObserver: ConnectivityObserver,
    private val flickrFetcherImpl: FlickrFetcher
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        PhotoGalleryViewModel(queryPreferences, networkConnectivityObserver, flickrFetcherImpl) as T
}