package com.bignerdranch.android.nerdlauncher.utils

fun <T> fastLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)