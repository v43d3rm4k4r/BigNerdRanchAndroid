package com.bignerdranch.android.criminalintent.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import kotlin.math.roundToInt

/**
 * Bitmap scaling for a specific Activity.
 */
fun getScaledBitmap(path: String, activity: Activity): Bitmap {
    val size = Point()
    activity.windowManager.defaultDisplay.getSize(size)

    return getScaledBitmap(path, size.x, size.y)
}

fun getScaledBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap {
    // Getting sized of image on disk
    var options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(path, options)

    val srcWidth  = options.outWidth.toFloat()
    val srcHeight = options.outHeight.toFloat()

    // Find out how much to reduce
    var inSampleSize = 1
    if (srcHeight > destHeight || srcWidth > srcWidth) {
        val heightScale = srcHeight / destHeight
        val widthScale  = srcWidth / destWidth

        val sampleScale = if (heightScale > widthScale) {
            heightScale
        } else {
            widthScale
        }
        inSampleSize = sampleScale.roundToInt()
    }

    options = BitmapFactory.Options()
    options.inSampleSize = inSampleSize

    // Reading and creating the final bitmap
    return BitmapFactory.decodeFile(path, options)
}