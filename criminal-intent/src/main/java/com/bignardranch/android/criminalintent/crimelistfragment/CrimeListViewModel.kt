package com.bignardranch.android.criminalintent.crimelistfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bignardranch.android.criminalintent.CrimeRepository

import com.bignardranch.android.criminalintent.model.Crime

class CrimeListViewModel : ViewModel() {
    private val crimeRepository = CrimeRepository.get()
    val crimeListLiveData: LiveData<List<Crime>> = crimeRepository.getCrimes()
}