package com.bignerdranch.android.photogallery.domain

sealed class FlickrFetcherSingleLiveEvent {

    object ErrorLoading : FlickrFetcherSingleLiveEvent()
}
