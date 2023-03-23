package com.bignerdranch.android.androidutils.livedata

import androidx.lifecycle.MediatorLiveData

open class NotNullMediatorLiveData<T> : MediatorLiveData<T>() {

    override fun getValue(): T =
        super.getValue() ?: throw IllegalStateException("live data not initialized")
}