package com.bignerdranch.android.photogallery.ui

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.SearchView

import androidx.core.view.MenuProvider

import com.bignerdranch.android.photogallery.R

/**
 * Menu provider used by [PhotoGalleryFragment].
 */
class PhotoGalleryMenuProvider(
    private val searchPhotos: (query: String) -> Unit
) : MenuProvider {

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.fragment_photo_gallery, menu)

        val searchItem = menu.findItem(R.id.menu_item_search)
        (searchItem.actionView as SearchView).apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String): Boolean {
                    searchPhotos(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean = false
            })
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when (menuItem.itemId) {
        R.id.menu_item_clear -> {
            // TODO
            true
        }
        else -> false
    }
}