package com.bignardranch.android.criminalintent.database

import java.util.UUID

import androidx.room.Dao
import androidx.room.Query

import com.bignardranch.android.criminalintent.Crime

@Dao
interface CrimeDao {
    @Query("SELECT * FROM crime")
    fun getCrimes(): List<Crime>

    @Query("SELECT * FROM crime WHERE id=(:id)")
    fun getCrime(id: UUID): Crime?
}