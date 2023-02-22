package com.bignerdranch.android.photogallery.presentation

import android.os.HandlerThread
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

class ThumbnailDownloader<in T> : HandlerThread(TAG),
    DefaultLifecycleObserver {

    private var hasQuit = false

    override fun quit(): Boolean {
        hasQuit = true
        return super.quit()
    }

    override fun onCreate(owner: LifecycleOwner) {
        Log.i(TAG, "Starting background thread")
        start()
        looper // ??
    }

    override fun onDestroy(owner: LifecycleOwner) {
        Log.i(TAG, "Exiting background thread")
        quit()
    }

    fun queueThumbnail(target: T, url: String) {
        Log.i(TAG, "Got a URL: $url")
    }

    private companion object {
        const val TAG = "ThumbnailDownloader"
    }
}