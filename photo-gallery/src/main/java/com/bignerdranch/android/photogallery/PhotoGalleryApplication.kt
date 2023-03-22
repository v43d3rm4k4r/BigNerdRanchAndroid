package com.bignerdranch.android.photogallery

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class PhotoGalleryApplication : Application() {

    /** Making channel on Oreo and higher. */
    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channelName = getString(R.string.notification_channel_name)
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "flickr_poll"
    }
}