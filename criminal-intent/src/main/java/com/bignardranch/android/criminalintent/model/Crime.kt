package com.bignardranch.android.criminalintent.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*
import kotlin.random.Random

@Entity
data class Crime(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val title: String = "",
    val date: Date = Date(),
    val isSolved: Boolean = false,
    val suspect: String = ""
) {
    @Ignore // missing from the database, so need to be ignored
    val requiresPolice: Boolean = Random.nextBoolean()
}
