package com.bignardranch.android.criminalintent.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*
import kotlin.random.Random

@Entity
data class Crime(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var date: Date = Date(),
    var isSolved: Boolean = false
) {
    @Ignore // missing from the database, so need to be ignored
    var requiresPolice: Boolean = Random.nextBoolean()
}
