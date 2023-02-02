package com.bignerdranch.android.nerdlauncher.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

inline fun <reified VM : ViewModel> ViewModelStoreOwner.lazyViewModel(
    noinline vmFactory: () -> VM
) = fastLazy {
    ViewModelProvider(this, LazyViewModelFactory(vmFactory))[VM::class.java]
}