package com.bignerdranch.android.androidutils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

import java.util.concurrent.atomic.AtomicBoolean

// TODO: MB extend it from NotNullMutableLiveData
class SingleLiveEvent<T> : MutableLiveData<T>() {

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