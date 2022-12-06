package com.bignardranch.beatbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignardranch.beatbox.databinding.ActivityMainBinding
import com.bignardranch.beatbox.databinding.ListItemSoundBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
        }
    }

    private inner class SoundHolder(private val binding: ListItemSoundBinding)
        : RecyclerView.ViewHolder(binding.root) {

    }

    private inner class SoundActivity()
        : RecyclerView.Adapter<SoundHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {

        }

        override fun onBindViewHolder(holder: SoundHolder, position: Int) { }

        override fun getItemCount(): Int = 0

    }
}