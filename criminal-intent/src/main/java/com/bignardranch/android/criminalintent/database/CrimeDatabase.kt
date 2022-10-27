package com.bignardranch.android.criminalintent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.bignardranch.android.criminalintent.model.Crime

@Database(entities = [Crime::class], version = 1)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase() {

    abstract fun crimeDao(): CrimeDao
}