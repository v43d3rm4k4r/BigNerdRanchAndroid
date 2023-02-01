package com.bignerdranch.android.nerdlauncher

import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper

import com.bignerdranch.android.nerdlauncher.databinding.ActivityNerdLauncherBinding
import com.bignerdranch.android.nerdlauncher.recyclerviewutils.ActivityAdapter
import com.bignerdranch.android.nerdlauncher.recyclerviewutils.SimpleItemTouchHelperCallback
import com.bignerdranch.android.nerdlauncher.utils.showToast

import kotlin.LazyThreadSafetyMode.NONE

// TODO: add view model
// TODO: add uninstall apps on swipe
// TODO: use ListAdapter?

class NerdLauncherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNerdLauncherBinding
    private val activities by lazy(NONE) { queryLaunchableActivities() }
    private val removeActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            showToast("App successfully removed")
        } else {
            showToast("Something goes wrong")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding = ActivityNerdLauncherBinding.inflate(layoutInflater).apply {
            setContentView(root)

            val adapter = ActivityAdapter(activities, packageManager, ::onActivityClicked, ::onActivityDeleted)
            appRecyclerView.adapter = adapter

            val callback = SimpleItemTouchHelperCallback(resources, adapter)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(appRecyclerView)

            val dividerItemDecoration = DividerItemDecoration(this@NerdLauncherActivity, LinearLayout.VERTICAL)
            appRecyclerView.addItemDecoration(dividerItemDecoration)
        }
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
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }

    private fun onActivityDeleted(position: Int) {
        val appToDelete = activities[position]
        val intent = Intent(Intent.ACTION_UNINSTALL_PACKAGE).apply {
            data = Uri.parse("package:${appToDelete.activityInfo.packageName}")
            putExtra(Intent.EXTRA_RETURN_RESULT, true)
        }
        removeActivityLauncher.launch(intent)
    }

    private companion object {
        const val TAG = "NerdLauncherActivity"
    }
}