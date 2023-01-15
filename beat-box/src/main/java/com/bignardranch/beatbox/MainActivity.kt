package com.bignardranch.beatbox

import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import com.bignardranch.beatbox.databinding.ActivityMainBinding
import com.bignardranch.beatbox.model.Sound
import com.bignardranch.beatbox.viewmodel.SoundViewModel

class MainActivity : AppCompatActivity(),
    SeekBar.OnSeekBarChangeListener {

    private lateinit var binding: ActivityMainBinding
    private val soundViewModel: SoundViewModel by viewModels { factory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.apply {
            adapter = SoundAdapter(beatBoxApplication.beatBox.sounds, ::onSoundClicked)
            addItemDecoration(SpacingItemDecorator((resources.displayMetrics.density * 4).toInt()))
        }

        binding.playSpeedSeekBar.setOnSeekBarChangeListener(this)

        soundViewModel.playbackSpeed.observe(this, Observer { value ->
            binding.playSpeedValue.text = getString(R.string.playback_speed, value)
            binding.playSpeedSeekBar.progress = value
        })
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

    /** [OnSeekBarChangeListener] methods: */
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) =
        soundViewModel.updatePlaybackSpeed(progress)

    override fun onStartTrackingTouch(seekBar: SeekBar?) { }
    override fun onStopTrackingTouch(seekBar: SeekBar?) { }
}