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

    private var hasQuit = false                  // TODO: MB delete
    private lateinit var requestHandler: Handler
    private val requestMap = ConcurrentHashMap<T, String>()

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
        looper // ??
    }

    fun queueThumbnail(target: T, url: String) {
        Log.i(TAG, "Got a URL: $url")
        requestMap[target] = url
        requestHandler.obtainMessage(MESSAGE_DOWNLOAD, target)
            .sendToTarget()
    }

    private fun handleRequest(target: T) {
        val url = requestMap[target] ?: return
        val bitmap = flickrFetcher.fetchPhoto(url) ?: return

        responseHandler.post( Runnable {
            if (requestMap[target] != url || hasQuit) { // WTF ?? зачем вообще эта проверка, и тем более в этой лямбде?
                return@Runnable
            }
            requestMap.remove(target) // почему здесь?
            onThumbnailDownloaded(target, bitmap) // вроде как единственное, что тут должно быть
        })
    }

    fun clearQueue() {
        Log.i(TAG, "Clearing all requests from queue")
        requestHandler.removeMessages(MESSAGE_DOWNLOAD)
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
    }
}