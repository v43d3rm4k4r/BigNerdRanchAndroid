package com.bignardranch.beatbox

import android.os.Bundle

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import com.bignardranch.beatbox.databinding.ActivityMainBinding
import com.bignardranch.beatbox.model.Sound
import com.bignardranch.beatbox.viewmodel.SoundViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val soundViewModel: SoundViewModel by viewModels { factory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.apply {
            adapter = SoundAdapter(beatBoxApplication.beatBox.sounds, ::onSoundClicked)
        }
    }

    private fun onSoundClicked(sound: Sound) = soundViewModel.onSoundClicked(sound)

    override fun onStart() {
        super.onStart()
        soundViewModel.loadSoundsIfNeeded()
    }

    override fun onStop() {
        super.onStop()
        soundViewModel.release()
    }
}