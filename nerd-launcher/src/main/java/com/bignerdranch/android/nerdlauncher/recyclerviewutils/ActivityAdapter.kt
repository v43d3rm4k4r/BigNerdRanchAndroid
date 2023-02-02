package com.bignerdranch.android.nerdlauncher.recyclerviewutils

import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.nerdlauncher.NerdLauncherUiItem
import com.bignerdranch.android.nerdlauncher.databinding.ListItemActivityBinding

class ActivityAdapter(
    private val onItemClicked: (resolveInfo: ResolveInfo) -> Unit,
    private val onItemSwapped: (position: Int) -> Unit
) : ListAdapter<NerdLauncherUiItem, ActivityAdapter.ActivityHolder>(ItemCallback),
    View.OnClickListener,
    ItemTouchHelperAdapter {

    override fun onClick(view: View) {
        val resolveInfo = view.tag as ResolveInfo
        onItemClicked(resolveInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemActivityBinding.inflate(layoutInflater, parent, false).apply {
            root.setOnClickListener(this@ActivityAdapter)
        }
        return ActivityHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityHolder, position: Int) =
        holder.bind(getItem(position))

    override fun onItemMove(fromPosition: Int, toPosition: Int) {}

    /**
     * [ItemTouchHelperAdapter] implementation:
     */
    override fun onItemDismiss(position: Int) = onItemSwapped(position)

    inner class ActivityHolder(private val binding: ListItemActivityBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NerdLauncherUiItem) =
            with(binding) {
                root.tag  = item.resolveInfo

                appImageView.setImageDrawable(item.icon)
                activityTitleTextView.text = item.title
            }
    }

    object ItemCallback : DiffUtil.ItemCallback<NerdLauncherUiItem>() {
        override fun areItemsTheSame(oldItem: NerdLauncherUiItem, newItem: NerdLauncherUiItem): Boolean =
            oldItem.resolveInfo.activityInfo.packageName == newItem.resolveInfo.activityInfo.packageName

        override fun areContentsTheSame(oldItem: NerdLauncherUiItem, newItem: NerdLauncherUiItem): Boolean =
            oldItem.resolveInfo.activityInfo.packageName == newItem.resolveInfo.activityInfo.packageName
    }
}
