package com.bignerdranch.android.geoquiz

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckedQuestion(var isChecked: Boolean = false,
                           var isCheated: Boolean = false) : Parcelable