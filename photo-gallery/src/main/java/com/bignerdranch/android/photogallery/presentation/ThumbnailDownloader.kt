package com.bignerdranch.android.photogallery.presentation

import android.graphics.Bitmap
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.util.Log

import com.bignerdranch.android.photogallery.domain.FlickrFetcher

import java.util.concurrent.ConcurrentHashMap

class ThumbnailDownloader<in T : Any>(
    private val flickrFetcher: FlickrFetcher,
    private val responseHandler: Handler,
    private val onThumbnailDownloaded: (T, Bitmap) -> Unit
) : HandlerThread(TAG) {

    private var hasQuit = false
    private var requestHandler: Handler? = null
    private val requestMap = ConcurrentHashMap<T, String>()
    private val lruCache = ThumbnailLruCache(ONE_FLICKR_PAGE_SIZE);

    @Suppress("UNCHECKED_CAST")
    //@SuppressLint("HandlerLeak")
    override fun onLooperPrepared() {
        requestHandler = object : Handler(Looper.myLooper()!!) {

            override fun handleMessage(msg: Message) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    val target = msg.obj as T
                    Log.i(TAG, "Got a request for URL: ${requestMap[target]}")
                    handleRequest(target)
                }
            }
        }
    }

    override fun start() {
        Log.i(TAG, "Starting background thread")
        super.start()
        looper
    }

    fun queueThumbnail(target: T, url: String) {
        Log.i(TAG, "Got a URL: $url")
        requestMap[target] = url
        requestHandler?.obtainMessage(MESSAGE_DOWNLOAD, target)
            ?.sendToTarget()
    }

    private fun handleRequest(target: T) {
        val url = requestMap[target] ?: return
        var bitmap = lruCache.getBitmapFromMemory(url)

        if (bitmap == null) {
            bitmap = flickrFetcher.fetchPhoto(url) ?: return
            lruCache.setBitmapToMemory(url, bitmap)
        }

        responseHandler.post( Runnable {
            if (requestMap[target] != url || hasQuit) {
                return@Runnable
            }
            requestMap.remove(target)
            onThumbnailDownloaded(target, bitmap)
        })
    }

    fun clearQueue() {
        Log.i(TAG, "Clearing all requests from queue")
        requestHandler?.removeMessages(MESSAGE_DOWNLOAD)
        requestMap.clear()
    }

    override fun quit(): Boolean {
        Log.i(TAG, "Exiting background thread")
        hasQuit = super.quit()
        return hasQuit
    }

    private companion object {
        const val TAG = "ThumbnailDownloader"
        const val MESSAGE_DOWNLOAD = 0
        const val ONE_FLICKR_PAGE_SIZE = 100
    }
}