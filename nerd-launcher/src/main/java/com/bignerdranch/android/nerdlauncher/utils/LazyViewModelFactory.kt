package com.bignerdranch.android.nerdlauncher.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LazyViewModelFactory<VM : ViewModel>(
    private val vmFactory: () -> VM,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return vmFactory() as T
    }
}