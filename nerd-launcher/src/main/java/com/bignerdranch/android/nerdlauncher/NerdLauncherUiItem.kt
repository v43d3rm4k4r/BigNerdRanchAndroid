package com.bignerdranch.android.nerdlauncher

import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable

class NerdLauncherUiItem(
    val data: ResolveInfo,
    val logo: Drawable?,
    val title: String
)
