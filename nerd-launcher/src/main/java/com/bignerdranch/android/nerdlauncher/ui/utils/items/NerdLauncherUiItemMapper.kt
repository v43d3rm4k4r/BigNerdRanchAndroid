package com.bignerdranch.android.nerdlauncher.ui.utils.items

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo

import com.bignerdranch.android.nerdlauncher.R

class NerdLauncherUiItemMapper(
    private val packageManager: PackageManager,
    private val context: Context,
) {

    fun map(item: ResolveInfo): NerdLauncherUiItem =
        NerdLauncherUiItem(
            resolveInfo = item,
            icon = item.loadIcon(packageManager),
            title = context.getString(
                R.string.activity_label_extended,
                item.loadLabel(packageManager).toString(),
                item.activityInfo.applicationInfo.packageName,
                item.activityInfo.name
            )
        )
}