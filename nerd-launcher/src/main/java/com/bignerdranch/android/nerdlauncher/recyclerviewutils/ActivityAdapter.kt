package com.bignerdranch.android.nerdlauncher.recyclerviewutils

import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.nerdlauncher.NerdLauncherUiItem
import com.bignerdranch.android.nerdlauncher.databinding.ListItemActivityBinding

class ActivityAdapter(
    private val activities: List<NerdLauncherUiItem>,
    private val onItemClicked: (resolveInfo: ResolveInfo) -> Unit,
    private val onItemSwapped: (position: Int) -> Unit
) : RecyclerView.Adapter<ActivityAdapter.ActivityHolder>(),
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

        fun bind(item: NerdLauncherUiItem) =
            with(binding) {
                root.tag  = item.data

                appImageView.setImageDrawable(item.logo)
                activityTitleTextView.text = item.title
            }
    }
}
