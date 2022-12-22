package com.bignardranch.beatbox.model

class Sound(val assetPath: String) {

    val name = assetPath.split('/').last().removeSuffix(WAV)

    private companion object {

        const val WAV = ".WAV"
    }
}