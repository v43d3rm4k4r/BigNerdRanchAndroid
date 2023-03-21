package com.bignerdranch.android.androidutils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.annotation.RequiresApi
import com.bignerdranch.android.androidutils.SingleLiveEvent
import com.bignerdranch.android.androidutils.network.ConnectivityObserverSingleLiveEvent.*

@RequiresApi(Build.VERSION_CODES.N)
class NetworkConnectivityObserver(
    context: Context
) : ConnectivityObserver {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override val events = SingleLiveEvent<ConnectivityObserverSingleLiveEvent>()

    private val callback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            events.postValue(NetworkStatus(ConnectivityObserver.Status.AVAILABLE))
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            events.postValue(NetworkStatus(ConnectivityObserver.Status.LOSING))
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            events.postValue(NetworkStatus(ConnectivityObserver.Status.LOST))
        }

        override fun onUnavailable() {
            super.onUnavailable()
            events.postValue(NetworkStatus(ConnectivityObserver.Status.UNAVAILABLE))
        }
    }

    init {
        connectivityManager.registerDefaultNetworkCallback(callback)
    }

//    @RequiresApi(Build.VERSION_CODES.N)
//    override fun observe(): Flow<ConnectivityObserver.Status> = callbackFlow {
//        val callback = object : ConnectivityManager.NetworkCallback() {
//
//            override fun onAvailable(network: Network) {
//                super.onAvailable(network)
//                launch { send(ConnectivityObserver.Status.Available) }
//            }
//
//            override fun onLosing(network: Network, maxMsToLive: Int) {
//                super.onLosing(network, maxMsToLive)
//                launch { send(ConnectivityObserver.Status.Losing) }
//            }
//
//            override fun onLost(network: Network) {
//                super.onLost(network)
//                launch { send(ConnectivityObserver.Status.Lost) }
//            }
//
//            override fun onUnavailable() {
//                super.onUnavailable()
//                launch { send(ConnectivityObserver.Status.Unavailable) }
//            }
//        }
//
//        connectivityManager.registerDefaultNetworkCallback(callback)
//        awaitClose {
//            connectivityManager.unregisterNetworkCallback(callback)
//        }
//    }.distinctUntilChanged()

    override fun onClear() = connectivityManager.unregisterNetworkCallback(callback)
}