package com.bignerdranch.android.nerdlauncher

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
import com.bignerdranch.android.nerdlauncher.presentation.NerdLauncherSingleLiveEvent
import com.bignerdranch.android.nerdlauncher.presentation.NerdLauncherSingleLiveEvent.ShowActivity
import com.bignerdranch.android.nerdlauncher.presentation.NerdLauncherSingleLiveEvent.ShowDeleteDialog
import com.bignerdranch.android.nerdlauncher.presentation.NerdLauncherSingleLiveEvent.ShowErrorDeletingApp
import com.bignerdranch.android.nerdlauncher.presentation.NerdLauncherSingleLiveEvent.ShowSuccessDeletingApp
import com.bignerdranch.android.nerdlauncher.presentation.NerdLauncherViewModel
import com.bignerdranch.android.nerdlauncher.recyclerviewutils.ActivityAdapter
import com.bignerdranch.android.nerdlauncher.recyclerviewutils.SimpleItemTouchHelperCallback
import com.bignerdranch.android.nerdlauncher.utils.NerdLauncherUiItemMapper
import com.bignerdranch.android.nerdlauncher.utils.fastLazy
import com.bignerdranch.android.nerdlauncher.utils.lazyViewModel
import com.bignerdranch.android.nerdlauncher.utils.showToast

// TODO: refactor packages

class NerdLauncherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNerdLauncherBinding
    private val viewModel by lazyViewModel { NerdLauncherViewModel(initQueryLaunchableActivities()) }
    private val adapter by fastLazy { ActivityAdapter(viewModel::onItemClick, viewModel::onItemDelete) }

    private val uiItemMapper by fastLazy { NerdLauncherUiItemMapper(packageManager, this) }

    private val removeActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        viewModel.handleActivityUninstallActionResult(result.resultCode)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        viewModel.state.observe(this, ::renderState)
        viewModel.events.observe(this, ::handleEvent)
    }

    private fun setupUI() {
        binding = ActivityNerdLauncherBinding.inflate(layoutInflater).apply {
            setContentView(root)

            appRecyclerView.adapter = adapter

            val callback = SimpleItemTouchHelperCallback(resources, adapter)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(appRecyclerView)

            val dividerItemDecoration = DividerItemDecoration(this@NerdLauncherActivity, LinearLayout.VERTICAL)
            appRecyclerView.addItemDecoration(dividerItemDecoration)
        }
    }

    private fun renderState(state: List<ResolveInfo>) {
        adapter.submitList(state.map(uiItemMapper::map))
    }

    private fun handleEvent(event: NerdLauncherSingleLiveEvent) {
        when (event) {
            is ShowDeleteDialog -> showDeleteDialog(event.resolveInfo)
            is ShowErrorDeletingApp -> {
                adapter.notifyItemChanged(event.itemIdx)
                showToast("Something goes wrong")
            }
            ShowSuccessDeletingApp -> showToast("App successfully removed")
            is ShowActivity -> showActivity(event.resolveInfo)
        }
    }

    private fun initQueryLaunchableActivities(): MutableList<ResolveInfo> {
        val startupIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val activities = packageManager.queryIntentActivities(startupIntent, 0).apply {
            // Sorting alphabetically
            sortWith { a, b ->
                String.CASE_INSENSITIVE_ORDER.compare(
                    a.loadLabel(packageManager).toString(),
                    b.loadLabel(packageManager).toString()
                )
            }
        }
        Log.i(TAG, "Found ${activities.size} activities")
        return activities
    }

    private fun showActivity(resolveInfo: ResolveInfo) {
        val activityInfo = resolveInfo.activityInfo

        val intent = Intent(Intent.ACTION_MAIN).apply {
            setClassName(activityInfo.applicationInfo.packageName, activityInfo.name)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }

    private fun showDeleteDialog(resolveInfo: ResolveInfo) {
        val intent = Intent(Intent.ACTION_UNINSTALL_PACKAGE).apply { // TODO: use new API
            data = Uri.parse("package:${resolveInfo.activityInfo.packageName}")
            putExtra(Intent.EXTRA_RETURN_RESULT, true)
        }
        removeActivityLauncher.launch(intent)
    }

    private companion object {
        const val TAG = "NerdLauncherActivity"
    }
}
