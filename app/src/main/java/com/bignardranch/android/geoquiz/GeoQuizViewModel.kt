package com.bignardranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "GeoQuizViewModel"

class GeoQuizViewModel : ViewModel() {
    init {
        Log.d(TAG, "ViewModel instance created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }
}