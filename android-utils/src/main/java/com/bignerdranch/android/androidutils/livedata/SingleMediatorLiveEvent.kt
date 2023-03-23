package com.bignerdranch.android.androidutils.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

import java.util.concurrent.atomic.AtomicBoolean

class SingleMediatorLiveEvent<T> : NotNullMediatorLiveData<T>() {

    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) { newValue ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(newValue)
            }
        }
    }

    override fun setValue(value: T) {
        pending.set(true)
        super.setValue(value)
    }
}