package com.example.fitnesstrackapp.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hydration_entries")
data class HydrationEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    /** Amount of water in millilitres recorded in this entry. */
    val amountMl: Int,
    /** UTC epoch milliseconds when this entry was recorded. */
    val timestamp: Long
)
