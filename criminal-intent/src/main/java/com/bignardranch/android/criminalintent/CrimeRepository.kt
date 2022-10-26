package com.bignardranch.android.criminalintent

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.bignardranch.android.criminalintent.database.CrimeDatabase
import com.bignardranch.android.criminalintent.model.Crime
import java.util.*

const val DATABASE_NAME = "crime-database"

/**
 * Must be initialized in [CriminalIntentApplication.onCreate]
 */
class CrimeRepository private constructor(context: Context) {

    private val database: CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val crimeDao = database.crimeDao()

    fun getCrimes(): LiveData<List<Crime>> = crimeDao.getCrimes()
    fun getCrime(id: UUID): LiveData<Crime?> = crimeDao.getCrime(id)

    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null)
                INSTANCE = CrimeRepository(context)
        }

        fun get(): CrimeRepository = INSTANCE ?:
            throw IllegalStateException("CrimeRepository must be initialized")
    }
}