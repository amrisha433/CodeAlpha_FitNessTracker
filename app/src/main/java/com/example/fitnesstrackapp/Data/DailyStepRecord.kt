package com.example.fitnesstrackapp.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Stores the sensor-based step count for a single calendar day.
 *
 * [date]                 ISO-8601 date string (yyyy-MM-dd) in the device's local timezone.
 * [steps]                Today's accumulated step count (sensor delta, never negative).
 * [cumulativeAtDayStart] The TYPE_STEP_COUNTER cumulative value observed at the start of [date].
 *                        Used to correctly calculate delta across reboots.
 */
@Entity(tableName = "daily_step_records")
data class DailyStepRecord(
    @PrimaryKey
    val date: String,           // "2024-11-05"
    val steps: Int,             // today's counted steps
    val cumulativeAtDayStart: Long  // sensor baseline at midnight
)
