package com.bignerdranch.beatbox

import android.app.Application
import com.bignerdranch.beatbox.model.BeatBox

class BeatBoxApplication : Application() {

    lateinit var beatBox: BeatBox

    override fun onCreate() {
        super.onCreate()
        beatBox = BeatBox(assets)
    }
}