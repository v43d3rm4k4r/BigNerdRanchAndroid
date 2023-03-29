package com.bignerdranch.android.photogallery

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import com.bignerdranch.android.photogallery.di.AppComponent
import com.bignerdranch.android.photogallery.di.DaggerAppComponent
import com.bignerdranch.android.photogallery.di.PresentationModule
import com.bignerdranch.android.photogallery.domain.notifications.CustomWorkerFactory
import javax.inject.Inject

class PhotoGalleryApplication : Application(), Configuration.Provider {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var customWorkerFactory: CustomWorkerFactory

    override fun onCreate() {
        super.onCreate()

        // Making channel on Oreo and higher:
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channelName = getString(R.string.notification_channel_name)
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

        appComponent = DaggerAppComponent.builder()
            .presentationModule(PresentationModule(this))
            .build()
            .apply { inject(this@PhotoGalleryApplication) }
    }

    override fun getWorkManagerConfiguration(): Configuration {
        val myWorkerFactory = DelegatingWorkerFactory()
        myWorkerFactory.addFactory(customWorkerFactory)
        return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .setWorkerFactory(myWorkerFactory)
            .build()
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "flickr_poll"
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is PhotoGalleryApplication -> appComponent
        else -> this.applicationContext.appComponent
    }