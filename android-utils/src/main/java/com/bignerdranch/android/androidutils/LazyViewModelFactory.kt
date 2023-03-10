package com.bignerdranch.android.androidutils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

import com.bignerdranch.android.kotlinutils.fastLazy

@Suppress("UNCHECKED_CAST")
class LazyViewModelFactory<VM : ViewModel>(
    private val vmFactory: () -> VM,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return vmFactory() as T
    }
}

inline fun <reified VM : ViewModel> ViewModelStoreOwner.fastLazyViewModel(
    noinline vmFactory: () -> VM
) = fastLazy {
    ViewModelProvider(this, LazyViewModelFactory(vmFactory))[VM::class.java]
}