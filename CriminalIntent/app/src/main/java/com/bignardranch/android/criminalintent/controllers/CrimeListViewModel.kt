package com.bignardranch.android.criminalintent.controllers

import androidx.lifecycle.ViewModel
import com.bignardranch.android.criminalintent.model.Crime
import kotlin.random.Random

class CrimeListViewModel : ViewModel() {

    val crimes = mutableListOf<Crime>()

    init {
        for (i in 0 until 10) {
            val crime = Crime().apply {
                title = "Crime #$i"
                isSolved = i % 2 == 0
                requiresPolice = !isSolved && Random.nextInt(10) in 0 until 5
            }
            crimes += crime
        }
    }
}