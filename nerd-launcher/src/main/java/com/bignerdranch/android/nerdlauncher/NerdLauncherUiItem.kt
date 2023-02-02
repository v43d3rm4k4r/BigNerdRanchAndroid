package com.bignerdranch.android.nerdlauncher

import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable

class NerdLauncherUiItem(
    val resolveInfo: ResolveInfo,
    val icon: Drawable?,
    val title: String,
)
