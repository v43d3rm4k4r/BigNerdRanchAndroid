package com.bignardranch.beatbox

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bignardranch.beatbox.viewmodel.SoundViewModel

class ViewModelFactory(
    private val app: BeatBoxApplication
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            SoundViewModel::class.java -> {
                SoundViewModel(app.beatBox)
            }
            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }
}

val Activity.beatBoxApplication: BeatBoxApplication
    get() = applicationContext as BeatBoxApplication

fun Activity.factory() = ViewModelFactory(beatBoxApplication)