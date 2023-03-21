package com.bignerdranch.android.androidutils.network

import com.bignerdranch.android.androidutils.SingleLiveEvent

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