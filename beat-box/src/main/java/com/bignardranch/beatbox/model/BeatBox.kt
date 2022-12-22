package com.bignardranch.beatbox.model

import android.content.res.AssetManager
import android.util.Log

// TODO: should this class belong to the model layer?
class BeatBox(
    private val assets: AssetManager
    ) {

    val sounds: List<Sound> = loadSounds()

    private fun loadSounds(): List<Sound> {
        val soundNames: Array<String>
        try {
            soundNames = assets.list(SOUNDS_FOLDER_NAME)!!
        } catch (exception: Exception) {
            Log.e(TAG, "Could not list assets", exception)
            return emptyList()
        }
        val sounds = mutableListOf<Sound>()
        soundNames.forEach { filename ->
            val assetPath = "$SOUNDS_FOLDER_NAME/$filename"
            sounds.add(Sound(assetPath))
        }
        return sounds
    }

    private companion object {

        const val TAG = "BeatBox"
        const val SOUNDS_FOLDER_NAME = "sample_sounds"
    }
}