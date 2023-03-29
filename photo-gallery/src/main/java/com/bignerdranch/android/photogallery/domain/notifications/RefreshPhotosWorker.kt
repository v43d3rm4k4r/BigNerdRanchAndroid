package com.bignerdranch.android.photogallery.domain.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bignerdranch.android.photogallery.PhotoGalleryApplication
import com.bignerdranch.android.photogallery.R
import com.bignerdranch.android.photogallery.data.FlickrFetcher
import com.bignerdranch.android.photogallery.data.QueryStore
import com.bignerdranch.android.photogallery.ui.PhotoGalleryActivity

class RefreshPhotosWorker(
    private val context: Context,
    workerParameters: WorkerParameters,
    private val sharedPreferences: QueryStore,
    private val flickrFetcher: FlickrFetcher,
) : Worker(context, workerParameters) {

    // TODO: test it:
    override fun doWork(): Result {
        Log.i(TAG, "Work request triggered")
        val query = sharedPreferences.getStoredQuery()
        val lastResultId = sharedPreferences.getLastResultId()
        val items = if (query.isEmpty()) {
            flickrFetcher.fetchInterestingPhotosRequest()
                .execute()
                .body()
                ?.photos
                ?.galleryItems
        } else {
            flickrFetcher.searchPhotosRequest(query)
                .execute()
                .body()
                ?.photos
                ?.galleryItems
        } ?: emptyList()

        if (items.isEmpty()) return Result.success()
        val resultId = items.first().id
        if (resultId == lastResultId) {
            Log.i(TAG, "Got an old result: $resultId")
        } else {
            Log.i(TAG, "Got a new result: $resultId")
            sharedPreferences.setLastResultId(resultId)

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED) {
                val intent = PhotoGalleryActivity.newIntent(context)
                val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

                val resources = context.resources
                val notification = NotificationCompat
                    .Builder(context, PhotoGalleryApplication.NOTIFICATION_CHANNEL_ID)
                    .setTicker(resources.getString(R.string.new_pictures_title))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(resources.getString(R.string.new_pictures_title))
                    .setContentText(resources.getString(R.string.new_pictures_text))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()
                NotificationManagerCompat.from(context).notify(0, notification)
            }
        }
        return Result.success()
    }

    private companion object {
        const val TAG = "RefreshPhotosWorker"
    }
}
