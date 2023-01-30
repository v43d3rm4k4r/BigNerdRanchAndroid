package com.bignerdranch.android.nerdlauncher

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bignerdranch.android.nerdlauncher.databinding.ActivityNerdLauncherBinding

class NerdLauncherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNerdLauncherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        setupAdapter()
    }

    private fun setupUI() {
        binding = ActivityNerdLauncherBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
    }

    private fun setupAdapter() {
        val activities = queryLaunchableActivities()
        binding.appRecyclerView.adapter = ActivityAdapter(activities, packageManager, ::onActivityClicked)
    }

    private fun queryLaunchableActivities(): List<ResolveInfo> {
        val startupIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val activities = packageManager.queryIntentActivities(startupIntent, 0).apply {
            // Sorting alphabetically
            sortWith(Comparator { a, b ->
                String.CASE_INSENSITIVE_ORDER.compare(
                    a.loadLabel(packageManager).toString(),
                    b.loadLabel(packageManager).toString()
                )
            })
        }
        Log.i(TAG, "Found ${activities.size} activities")
        return activities
    }

    private fun onActivityClicked(resolveInfo: ResolveInfo) {
        val activityInfo = resolveInfo.activityInfo

        val intent = Intent(Intent.ACTION_MAIN).apply {
            setClassName(activityInfo.applicationInfo.packageName, activityInfo.name)
        }
        startActivity(intent)
    }

    private companion object {
        const val TAG = "NerdLauncherActivity"
    }
}