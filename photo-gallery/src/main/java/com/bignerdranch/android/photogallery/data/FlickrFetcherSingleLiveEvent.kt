package com.bignerdranch.android.photogallery.data

sealed class FlickrFetcherSingleLiveEvent {

    object ErrorLoading : FlickrFetcherSingleLiveEvent()
}
