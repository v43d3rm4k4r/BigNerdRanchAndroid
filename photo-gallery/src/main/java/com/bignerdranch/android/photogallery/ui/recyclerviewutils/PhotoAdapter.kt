package com.bignerdranch.android.photogallery.ui.recyclerviewutils

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.bignerdranch.android.photogallery.R
import com.bignerdranch.android.photogallery.databinding.ListItemGalleryBinding
import com.bignerdranch.android.photogallery.domain.model.GalleryItem

class PhotoAdapter(
    private val onItemClicked: (galleryItem: GalleryItem) -> Unit,
    private val queueThumbnail: (target: PhotoHolder, url: String) -> Unit
) : ListAdapter<GalleryItem, PhotoAdapter.PhotoHolder>(ItemCallback),
    View.OnClickListener {

    override fun onClick(view: View) {
        val galleryItem = view.tag as GalleryItem
        onItemClicked(galleryItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemGalleryBinding.inflate(layoutInflater, parent, false).apply {
            root.setOnClickListener(this@PhotoAdapter)
        }
        return PhotoHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val galleryItem = getItem(position)
        holder.bind(galleryItem)
        queueThumbnail(holder, galleryItem.url)
    }
//    /**
//     * [ItemTouchHelperAdapter] implementation:
//     */
//    override fun onItemDismiss(position: Int) = onItemSwapped(getItem(position).resolveInfo)

    inner class PhotoHolder(private val binding: ListItemGalleryBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GalleryItem) =
            with(binding) {
                root.tag  = item
                val placeholder = ContextCompat.getDrawable(
                    root.context,
                    R.drawable.mda
                ) ?: ColorDrawable()
                root.setImageDrawable(placeholder)
            }

        fun bindDrawable(drawable: Drawable) =
            with(binding) {
                root.setImageDrawable(drawable)
            }
    }

    object ItemCallback : DiffUtil.ItemCallback<GalleryItem>() {
        override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean =
            oldItem == newItem
    }
}