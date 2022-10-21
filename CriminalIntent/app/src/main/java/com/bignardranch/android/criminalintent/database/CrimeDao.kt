package com.bignardranch.android.criminalintent.database

import java.util.UUID

import androidx.room.Dao
import androidx.room.Query
import androidx.lifecycle.LiveData

import com.bignardranch.android.criminalintent.model.Crime

@Dao
interface CrimeDao {
    @Query("SELECT * FROM crime")
    fun getCrimes(): LiveData<List<Crime>>

    @Query("SELECT * FROM crime WHERE id=(:id)")
    fun getCrime(id: UUID): LiveData<Crime?>
}