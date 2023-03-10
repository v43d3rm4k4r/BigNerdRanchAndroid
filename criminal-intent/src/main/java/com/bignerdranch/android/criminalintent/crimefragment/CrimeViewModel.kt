package com.bignerdranch.android.criminalintent.crimefragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.bignerdranch.android.criminalintent.CrimeRepository
import com.bignerdranch.android.criminalintent.model.Crime

import java.io.File
import java.util.*

class CrimeViewModel : ViewModel() {

    private val crimeRepository = CrimeRepository.get()
    private val crimeIdLiveData = MutableLiveData<UUID>() // current crime
    var crimeLiveData: LiveData<Crime?> = Transformations.switchMap(crimeIdLiveData) { crimeId ->
        crimeRepository.getCrime(crimeId)
    }

    fun loadCrime(crimeId: UUID) {
        crimeIdLiveData.value = crimeId
    }

    fun updateCrime(crime: Crime) = crimeRepository.updateCrime(crime)
    fun deleteCrime(crime: Crime) = crimeRepository.deleteCrime(crime)

    fun getPhotoFile(crime: Crime): File = crimeRepository.getPhotoFile(crime)
}