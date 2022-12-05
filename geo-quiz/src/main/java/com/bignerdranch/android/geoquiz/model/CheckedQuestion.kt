package com.bignerdranch.android.geoquiz.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckedQuestion(var isChecked: Boolean = false,
                           var isCheated: Boolean = false) : Parcelable