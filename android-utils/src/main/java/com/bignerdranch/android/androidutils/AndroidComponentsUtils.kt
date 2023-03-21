package com.bignerdranch.android.androidutils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

import com.google.android.material.snackbar.Snackbar

fun LifecycleOwner.showToast(msg: CharSequence) {
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

fun LifecycleOwner.showSnackbar(msg: CharSequence, duration: Int = Snackbar.LENGTH_LONG) {
    val view = when (this) {
        is Activity -> findViewById<View>(android.R.id.content)
        is Fragment -> requireActivity().findViewById(android.R.id.content)
        else        -> return
    }
    Snackbar.make(view, msg, duration).show()
}

fun LifecycleOwner.showSnackbar(@StringRes resId: Int, duration: Int = Snackbar.LENGTH_LONG) {
    val str = when (this) {
        is Activity -> getString(resId)
        is Fragment -> getString(resId)
        else        -> return
    }
    showSnackbar(str, duration)
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