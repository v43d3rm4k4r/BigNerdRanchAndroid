package com.bignerdranch.android.photogallery.domain.notifications

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.bignerdranch.android.photogallery.data.FlickrFetcher
import com.bignerdranch.android.photogallery.data.QueryStore
import javax.inject.Inject

class CustomWorkerFactory @Inject constructor(
    private val sharedPreferences: QueryStore,
    private val flickrFetcher: FlickrFetcher,
) : WorkerFactory() {

    override fun createWorker(appContext: Context, workerClassName: String, workerParameters: WorkerParameters): ListenableWorker? {
        return when (workerClassName) {
            RefreshPhotosWorker::class.java.name -> RefreshPhotosWorker(appContext, workerParameters, sharedPreferences, flickrFetcher)
            else -> null
        }
    }
}
