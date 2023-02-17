package com.bignerdranch.android.photogallery.presentation

import android.os.HandlerThread
import android.util.Log

class ThumbnailDownloader<in T> : HandlerThread(TAG) {

    private var hasQuit = false

    override fun quit(): Boolean {
        hasQuit = true
        return super.quit()
    }

    fun queueThumbnail(target: T, url: String) {
        Log.i(TAG, "Got a URL: $url")
    }

    private companion object {
        const val TAG = "ThumbnailDownloader"
    }
}