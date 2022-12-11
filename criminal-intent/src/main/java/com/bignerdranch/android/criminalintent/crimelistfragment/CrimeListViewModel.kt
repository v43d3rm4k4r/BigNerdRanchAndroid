package com.bignerdranch.android.criminalintent.crimelistfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.criminalintent.CrimeRepository
import com.bignerdranch.android.criminalintent.model.Crime

class CrimeListViewModel : ViewModel() {

    private val crimeRepository = CrimeRepository.get()
    val crimeListLiveData: LiveData<List<Crime>> = crimeRepository.getCrimes()

    fun addCrime(crime: Crime)    = crimeRepository.addCrime(crime)
    fun deleteCrime(crime: Crime) = crimeRepository.deleteCrime(crime)
}