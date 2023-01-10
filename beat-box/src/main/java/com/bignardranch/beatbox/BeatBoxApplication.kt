package com.bignardranch.beatbox

import android.app.Application
import com.bignardranch.beatbox.model.BeatBox

class BeatBoxApplication : Application() {

    val beatBox by lazy { BeatBox(assets) }
}