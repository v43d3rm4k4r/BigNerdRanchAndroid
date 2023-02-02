package com.bignerdranch.android.nerdlauncher.recyclerviewutils

import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.bignerdranch.android.nerdlauncher.R
import com.bignerdranch.android.nerdlauncher.databinding.ListItemActivityBinding

class ActivityAdapter(
    private val activities: List<ResolveInfo>,
    private val packageManager: PackageManager,
    private val onItemClicked: (resolveInfo: ResolveInfo) -> Unit,
    private val onItemSwapped: (position: Int) -> Unit
) : ListAdapter<ResolveInfo, ActivityAdapter.ActivityHolder>(ItemCallback),
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
        holder.bind(activities[position])

    override fun getItemCount(): Int = activities.size

    override fun onItemMove(fromPosition: Int, toPosition: Int) {}

    /**
     * [ItemTouchHelperAdapter] implementation:
     */
    override fun onItemDismiss(position: Int) = onItemSwapped(position)

    inner class ActivityHolder(private val binding: ListItemActivityBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(resolveInfo: ResolveInfo) =
            with(binding) {
                root.tag  = resolveInfo

                appImageView.setImageDrawable(resolveInfo.loadIcon(packageManager))

                activityTitleTextView.text = root.context.getString(
                    R.string.activity_label_extended,
                    resolveInfo.loadLabel(packageManager).toString(),
                    resolveInfo.activityInfo.applicationInfo.packageName,
                    resolveInfo.activityInfo.name
                )
            }
    }

    object ItemCallback : DiffUtil.ItemCallback<ResolveInfo>() {
        override fun areItemsTheSame(oldItem: ResolveInfo, newItem: ResolveInfo): Boolean =
            oldItem.activityInfo.packageName == newItem.activityInfo.packageName

        override fun areContentsTheSame(oldItem: ResolveInfo, newItem: ResolveInfo): Boolean =
            oldItem.activityInfo.packageName == newItem.activityInfo.packageName
    }
}