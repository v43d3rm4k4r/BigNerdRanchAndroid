package com.bignerdranch.android.nerdlauncher.utils

import androidx.lifecycle.MutableLiveData

class NotNullableMutableLiveData<T>(value: T) : MutableLiveData<T>(value) {

    override fun getValue(): T {
        return super.getValue() ?: throw IllegalStateException("live data not initialized")
    }
}
