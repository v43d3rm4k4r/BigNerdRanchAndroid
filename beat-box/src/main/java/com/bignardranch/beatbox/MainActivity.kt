package com.bignardranch.beatbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

import com.bignardranch.beatbox.databinding.ActivityMainBinding
import com.bignardranch.beatbox.databinding.ListItemSoundBinding
import com.bignardranch.beatbox.model.BeatBox
import com.bignardranch.beatbox.model.Sound

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var binding: ActivityMainBinding
    private lateinit var beatBox: BeatBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        beatBox = BeatBox(assets)

        binding.recyclerView.apply {
            adapter = SoundAdapter(beatBox.sounds)
        }
    }

    private inner class SoundHolder(private val binding: ListItemSoundBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(sound: Sound) {
            with(binding) {
                soundButton.tag = sound
                soundButton.text = sound.name
            }
        }
    }

    private inner class SoundAdapter(private val sounds: List<Sound>) : RecyclerView.Adapter<SoundHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(viewType, parent, false)
            return SoundHolder(ListItemSoundBinding.bind(view))
        }

        override fun onBindViewHolder(holder: SoundHolder, position: Int) =
            holder.bind(sounds[position])

        override fun getItemCount(): Int = sounds.size
    }
}