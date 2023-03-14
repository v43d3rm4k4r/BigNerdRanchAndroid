package com.bignerdranch.android.androidutils

import android.app.Activity
import android.view.inputmethod.InputMethodManager
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

fun LifecycleOwner.closeKeyboard() {
    val activity: Activity = when (this) {
        is Activity -> this
        is Fragment -> requireActivity()
        else        -> return
    }
    val view = activity.currentFocus ?: return
    val manager = activity.getSystemService(
        Activity.INPUT_METHOD_SERVICE
    ) as InputMethodManager?
    manager?.hideSoftInputFromWindow(view.windowToken, 0)
}