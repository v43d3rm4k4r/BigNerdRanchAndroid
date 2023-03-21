package com.bignerdranch.android.androidutils.network

sealed class ConnectivityObserverSingleLiveEvent {

    class NetworkStatus(val status: ConnectivityObserver.Status) : ConnectivityObserverSingleLiveEvent()
}