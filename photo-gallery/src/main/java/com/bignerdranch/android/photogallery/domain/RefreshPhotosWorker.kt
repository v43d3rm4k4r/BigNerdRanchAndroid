package com.bignerdranch.android.photogallery.domain

import android.content.Context
import android.util.Log

import androidx.work.Worker
import androidx.work.WorkerParameters

import com.bignerdranch.android.photogallery.data.FlickrFetcher
import com.bignerdranch.android.photogallery.data.QueryPreferences

class RefreshPhotosWorker(
    context: Context, // TODO: USE APPLICATION CONTEXT
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    private val sharedPreferences = QueryPreferences(context)

    override fun doWork(): Result {
        Log.i(TAG, "Work request triggered")
        val query = sharedPreferences.getStoredQuery()
        val lastResultId = sharedPreferences.getLastResultId()
        val items = if (query.isEmpty()) {
            FlickrFetcher().fetchInterestingPhotosRequest()
                .execute()
                .body()
                ?.photos
                ?.galleryItems
        } else {
            FlickrFetcher().searchPhotosRequest(query)
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
        }

        return Result.success()
    }

    private companion object {
        const val TAG = "RefreshPhotosWorker"
    }
}