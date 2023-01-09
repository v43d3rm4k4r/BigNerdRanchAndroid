package com.bignardranch.beatbox.model

class Sound(val assetPath: String,
            var soundId: Int? = null
) {

    val name = assetPath.split('/').last().removeSuffix(".wav")
}