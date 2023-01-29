package com.bignerdranch.android.nerdlauncher

import android.content.Intent
import android.content.pm.PackageManager
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
        val startupIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val activities = packageManager.queryIntentActivities(startupIntent, 0)
        activities.sortWith(Comparator { a, b ->
            String.CASE_INSENSITIVE_ORDER.compare(
                a.loadLabel(packageManager).toString(),
                b.loadLabel(packageManager).toString()
            )
        })

        Log.i(TAG, "Found ${activities.size} activities")
    }

//    private fun resolveActivityFor(view: View, intent: Intent): Boolean {
//        val resolvedActivity = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
//        if (resolvedActivity == null) {
//            view.isEnabled = false
//            return false
//        }
//        return true
//    }

    private companion object {
        const val TAG = "NerdLauncherActivity"
    }
}