package com.bignerdranch.android.nerdlauncher

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.annotation.StringRes

class NerdLauncherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context private set
    }
}

object Strings {

    fun get(@StringRes stringRes: Int, vararg formatArgs: Any = emptyArray()): String =
        NerdLauncherApplication.context.getString(stringRes, *formatArgs)
}