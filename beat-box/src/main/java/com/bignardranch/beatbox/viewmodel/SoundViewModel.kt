package com.bignardranch.beatbox.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bignardranch.beatbox.model.BeatBox

import com.bignardranch.beatbox.model.Sound

class SoundViewModel(
    private val beatBox: BeatBox
) : ViewModel() {

//    var sound: Sound? = null
//        set(sound) {
//            field = sound
//            _title.postValue(sound?.name)
//        }
//    private val _sound: MutableLiveData<Sound?> = MutableLiveData()
//    val sound: LiveData<Sound?> = _sound

//    private val _title: MutableLiveData<String?> = MutableLiveData()
//    val title: LiveData<String?> = _title
//    var titleLiveData: LiveData<String?> = Transformations.switchMap(_sound) { sound ->
//        MutableLiveData(sound?.name)
//    }

    fun loadSoundsIfNeeded() = beatBox.loadSoundsIfNeeded()

    fun release() = beatBox.release()

    fun onSoundClicked(sound: Sound) =
        sound.let {
            beatBox.play(it)
        }
}