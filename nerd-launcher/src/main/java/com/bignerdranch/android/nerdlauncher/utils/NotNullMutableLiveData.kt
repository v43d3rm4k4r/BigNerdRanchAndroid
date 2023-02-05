package com.bignerdranch.android.nerdlauncher.utils

import androidx.lifecycle.MutableLiveData

class NotNullMutableLiveData<T>(value: T) : MutableLiveData<T>(value) {

    override fun getValue(): T =
        super.getValue() ?: throw IllegalStateException("live data not initialized")
}