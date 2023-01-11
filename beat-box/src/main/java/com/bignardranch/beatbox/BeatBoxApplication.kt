package com.bignardranch.beatbox

import android.app.Application
import com.bignardranch.beatbox.model.BeatBox

class BeatBoxApplication : Application() {

    lateinit var beatBox: BeatBox

    override fun onCreate() {
        super.onCreate()
        beatBox = BeatBox(assets)
    }
}