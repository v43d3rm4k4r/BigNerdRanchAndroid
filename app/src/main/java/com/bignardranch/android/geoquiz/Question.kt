package com.bignardranch.android.geoquiz

import android.support.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: Boolean)