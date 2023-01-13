package com.bignardranch.beatbox.model

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.SoundPool
import android.util.Log

import java.io.IOException

/**
 * - Loads sounds from assets
 * - Plays the specified sound
 */
class BeatBox(
    private val assets: AssetManager
) {

    private var soundPool: SoundPool? = buildSoundPool()
    val sounds: List<Sound> = loadSounds()

    private fun buildSoundPool(): SoundPool = SoundPool.Builder()
        .setMaxStreams(MAX_SOUNDS)
        .build()

    fun loadSoundsIfNeeded() {
        if (soundPool == null) soundPool = buildSoundPool()
        loadSounds()
    }

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
            val sound = Sound(assetPath)
            try {
                load(sound)
                sounds.add(sound)
            } catch (ioe: IOException) {
                Log.e(TAG, "Could not load sound $filename", ioe)
            }
        }
        return sounds
    }

    private fun load(sound: Sound) {
        val afd: AssetFileDescriptor = assets.openFd(sound.assetPath)
        val soundId = soundPool?.load(afd, 1)
        sound.soundId = soundId
    }

    fun play(sound: Sound, rate: Int = 100) {
        sound.soundId?.let {
            soundPool?.play(it, 1.0f, 1.0f, 1, 0, rate.toFloat() / 100)
        }
    }

    fun release() {
        soundPool?.release()
        soundPool = null
    }

    private companion object {

        const val TAG = "BeatBox"
        const val SOUNDS_FOLDER_NAME = "sample_sounds"
        const val MAX_SOUNDS = 5
    }
}