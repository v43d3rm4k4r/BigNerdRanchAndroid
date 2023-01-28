package com.bignerdranch.beatbox

import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import com.bignerdranch.beatbox.databinding.ActivityMainBinding
import com.bignerdranch.beatbox.model.Sound
import com.bignerdranch.beatbox.viewmodel.SoundViewModel

class MainActivity : AppCompatActivity(),
    OnSeekBarChangeListener {

    private lateinit var binding: ActivityMainBinding
    private val soundViewModel: SoundViewModel by viewModels { factory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        with(binding) {
            setContentView(root)
            recyclerView.adapter = SoundAdapter(beatBoxApplication.beatBox.sounds, ::onSoundClicked)
//            addItemDecoration(SpacingItemDecorator((resources.displayMetrics.density * 4).toInt()))
            playSpeedSeekBar.setOnSeekBarChangeListener(this@MainActivity)

            soundViewModel.playbackSpeed.observe(this@MainActivity, Observer { value ->
                playSpeedValue.text = getString(R.string.playback_speed, value)
                playSpeedSeekBar.progress = value
            })
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

    /** [OnSeekBarChangeListener] methods: */
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) =
        soundViewModel.updatePlaybackSpeed(progress)

    override fun onStartTrackingTouch(seekBar: SeekBar?) { }
    override fun onStopTrackingTouch(seekBar: SeekBar?) { }
}