package com.bignerdranch.beatbox.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.bignerdranch.beatbox.model.BeatBox
import com.bignerdranch.beatbox.model.Sound

class SoundViewModel(
    private val beatBox: BeatBox
) : ViewModel() {

    private val _playbackSpeed = MutableLiveData(100)
    val playbackSpeed: LiveData<Int> = _playbackSpeed

    fun loadSoundsIfNeeded() = beatBox.loadSoundsIfNeeded()

    fun release() = beatBox.release()

    fun onSoundClicked(sound: Sound) =
        sound.let {
            beatBox.play(it, playbackSpeed.value!!)
        }

    fun updatePlaybackSpeed(value: Int) { _playbackSpeed.value = value }
}