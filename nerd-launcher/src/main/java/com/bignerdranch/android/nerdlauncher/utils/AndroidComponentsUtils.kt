package com.bignerdranch.android.nerdlauncher.utils

import android.app.Activity
import android.widget.Toast

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

fun LifecycleOwner.showToast(msg: String) {
    val context = when (this) {
        is Activity -> this
        is Fragment -> requireContext()
        else        -> return
    }
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun LifecycleOwner.showToast(@StringRes resId: Int) {
    val str = when (this) {
        is Activity -> getString(resId)
        is Fragment -> getString(resId)
        else        -> return
    }
    showToast(str)
}