package com.bignerdranch.beatbox

import android.app.Activity

val Activity.beatBoxApplication: BeatBoxApplication
    get() = applicationContext as BeatBoxApplication