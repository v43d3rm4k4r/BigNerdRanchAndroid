package com.bignerdranch.android.photogallery.domain

import android.graphics.Bitmap
import android.util.LruCache

class ThumbnailLruCache(maxSize: Int) : LruCache<String, Bitmap>(maxSize) {

    fun getBitmapFromMemory(key: String): Bitmap? = this[key]

    fun setBitmapToMemory(key: String, bitmap: Bitmap): Bitmap? {
        if (getBitmapFromMemory(key) == null) {
            return put(key, bitmap)
        }
        return null
    }
}