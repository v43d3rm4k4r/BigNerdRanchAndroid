package com.bignardranch.android.criminalintent

import androidx.lifecycle.ViewModel
import kotlin.random.Random


class CrimeListViewModel : ViewModel() {

    val crimes = mutableListOf<Crime>()

    init {
        for (i in 0 until 5) {
            val crime = Crime().apply {
                title = "Crime #$i"
                isSolved = i % 2 == 0
                requiresPolice = Random.nextInt(10) in 0..3
            }
            crimes += crime
        }
    }
}