package com.bignerdranch.android.kotlinutils

fun <T> fastLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)