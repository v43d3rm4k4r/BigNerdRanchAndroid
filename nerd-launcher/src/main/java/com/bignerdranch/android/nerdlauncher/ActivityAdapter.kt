package com.bignerdranch.android.nerdlauncher

import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.nerdlauncher.databinding.ListItemActivityBinding

class ActivityAdapter(
    private val activities: List<ResolveInfo>,
    private val packageManager: PackageManager,
    private val onItemClicked: (resolveInfo: ResolveInfo) -> Unit
) : RecyclerView.Adapter<ActivityAdapter.ActivityHolder>(),
    View.OnClickListener {

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

    inner class ActivityHolder(private val binding: ListItemActivityBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(resolveInfo: ResolveInfo) {
            with(binding) {
                root.tag  = resolveInfo
                activityTitleTextView.text = resolveInfo.loadLabel(packageManager).toString()
            }
        }
    }
}