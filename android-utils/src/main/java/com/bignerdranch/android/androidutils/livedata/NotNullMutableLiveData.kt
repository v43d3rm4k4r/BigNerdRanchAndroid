package com.bignerdranch.android.androidutils.livedata

import androidx.lifecycle.MutableLiveData

open class NotNullMutableLiveData<T> : MutableLiveData<T>() {

    override fun getValue(): T =
        super.getValue() ?: throw IllegalStateException("live data not initialized")
}