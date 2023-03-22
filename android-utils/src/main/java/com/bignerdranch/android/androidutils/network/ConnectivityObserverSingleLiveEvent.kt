package com.bignerdranch.android.androidutils.network

/**
 * Make sealed if new events appear.
 */
open class ConnectivityObserverSingleLiveEvent {

    class NetworkStatus(val status: ConnectivityObserver.Status) : ConnectivityObserverSingleLiveEvent()
}