package com.bignerdranch.android.androidutils.network

import com.bignerdranch.android.androidutils.livedata.SingleLiveEvent

interface ConnectivityObserver {

    val events: SingleLiveEvent<ConnectivityObserverSingleLiveEvent>

    fun onClear()

    enum class Status {
        AVAILABLE,
        UNAVAILABLE,
        LOSING,
        LOST
    }
}