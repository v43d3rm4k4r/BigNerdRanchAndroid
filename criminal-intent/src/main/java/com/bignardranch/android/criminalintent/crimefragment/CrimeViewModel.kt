package com.bignardranch.android.criminalintent.crimefragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bignardranch.android.criminalintent.CrimeRepository
import com.bignardranch.android.criminalintent.model.Crime
import java.util.*

class CrimeViewModel : ViewModel() {

    private val crimeRepository = CrimeRepository.get()
    private val crimeIdLiveData = MutableLiveData<UUID>() // current crime
    var crimeLiveData: LiveData<Crime?> = Transformations.switchMap(crimeIdLiveData) { crimeId ->
        crimeRepository.getCrime(crimeId)
    }
    // TODO: mb use public LiveData<Crime?> property instead?

    fun loadCrime(crimeId: UUID) {
        crimeIdLiveData.value = crimeId
    }
}