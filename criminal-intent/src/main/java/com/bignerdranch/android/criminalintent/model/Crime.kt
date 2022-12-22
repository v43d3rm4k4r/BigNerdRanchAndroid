package com.bignerdranch.android.criminalintent.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Crime(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val title: String = "",
    val date: Date = Date(),
    val isSolved: Boolean = false,
    val suspect: String = "",
    val requiresPolice: Boolean = false
) {
    val photoFileName
        get() = "IMG_$id.jpg"
}