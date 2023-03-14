package com.bignerdranch.android.photogallery.ui

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider

import com.bignerdranch.android.photogallery.R
import com.bignerdranch.android.photogallery.presentation.PhotoGalleryViewModel

/** Menu provider used by [PhotoGalleryFragment]. */
class PhotoGalleryMenuProvider(
    private val viewModel: PhotoGalleryViewModel,
    private val hideKeyboard: () -> Unit
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
            else -> false
        }
}