package com.bignerdranch.android.photogallery.ui

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.bignerdranch.android.photogallery.R
import com.bignerdranch.android.photogallery.data.QueryStore
import com.bignerdranch.android.photogallery.domain.notifications.RefreshPhotosWorker
import com.bignerdranch.android.photogallery.presentation.PhotoGalleryViewModel
import java.util.concurrent.TimeUnit

/** Menu provider used by [PhotoGalleryFragment]. */
class PhotoGalleryMenuProvider(
    private val context: Context,
    private val viewModel: PhotoGalleryViewModel,
    private val hideKeyboard: () -> Unit,
    private val queryStore: QueryStore
) : MenuProvider {

    private var searchView: SearchView? = null

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.fragment_photo_gallery, menu)

        val searchItem = menu.findItem(R.id.menu_item_search)
        searchView = (searchItem.actionView as SearchView).apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.searchPhotos(query)
                    hideKeyboard()
                    onActionViewCollapsed()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean = false
            })
            setOnSearchClickListener {
                searchView?.setQuery(viewModel.searchTerm, false)
            }
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.menu_item_clear -> {
                viewModel.searchPhotos("")
                searchView?.isIconified = true
                true
            }
            R.id.menu_item_toggle_polling -> {
                val isPolling = queryStore.isPolling()
                val workManager = WorkManager.getInstance(context)
                if (isPolling) {
                    workManager.cancelUniqueWork(POLL_WORK)
                    queryStore.setPolling(false) // TODO: refactor this
                    menuItem.title = context.getString(R.string.start_polling)
                } else {
                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.UNMETERED)
                        .build()
                    val periodicRequest = PeriodicWorkRequest.Builder(RefreshPhotosWorker::class.java, 15, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .build()
                    workManager.enqueueUniquePeriodicWork(POLL_WORK, ExistingPeriodicWorkPolicy.KEEP, periodicRequest)
                    queryStore.setPolling(true)
                    menuItem.title = context.getString(R.string.stop_polling)
                }
                true
            }
            else -> false
        }

    private companion object {
        const val POLL_WORK = "POLL_WORK"
    }
}
