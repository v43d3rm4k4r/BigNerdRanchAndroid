package com.bignardranch.android.criminalintent.controllers

import android.content.Context
import androidx.room.Room
import com.bignardranch.android.criminalintent.database.CrimeDatabase

class CrimeRepository private constructor(context: Context) {

    private val database: CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        "crime-database"
    ).build()

    private val crimeDao = database.crimeDao()

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