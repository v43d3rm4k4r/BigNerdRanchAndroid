package com.bignardranch.beatbox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView

import com.bignardranch.beatbox.databinding.ListItemSoundBinding
import com.bignardranch.beatbox.model.Sound

class SoundAdapter(
    private val sounds: List<Sound>,
    private val onItemClicked: (sound: Sound) -> Unit,
) : RecyclerView.Adapter<SoundAdapter.SoundHolder>(),
    View.OnClickListener {

    override fun onClick(view: View) {
        val sound = view.tag as Sound
        onItemClicked(sound)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemSoundBinding.inflate(layoutInflater).apply {
            soundButton.setOnClickListener(this@SoundAdapter)
        }
        return SoundHolder(binding)
    }

    override fun onBindViewHolder(holder: SoundHolder, position: Int) =
        holder.bind(sounds[position])

    override fun getItemCount(): Int = sounds.size

    class SoundHolder(private val binding: ListItemSoundBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(sound: Sound) {
            with(binding) {
                soundButton.tag  = sound
                soundButton.text = sound.name.drop(3) // removing weird prefixes
            }
        }
    }
}