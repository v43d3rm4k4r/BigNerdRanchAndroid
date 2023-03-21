package com.bignerdranch.android.photogallery.model

sealed class FlickrFetcherSingleLiveEvent {

    object ErrorLoading : FlickrFetcherSingleLiveEvent()
}
