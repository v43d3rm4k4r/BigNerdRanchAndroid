package com.bignerdranch.android.nerdlauncher.ui.utils.items

import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable

data class NerdLauncherUiItem(
    val resolveInfo: ResolveInfo,
    val icon: Drawable?,
    val title: String,
)