package com.example.fitnesstrackapp.Data

import androidx.room.*

@Dao
interface StepDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(record: DailyStepRecord)

    @Query("SELECT * FROM daily_step_records WHERE `date` = :date LIMIT 1")
    suspend fun getForDate(date: String): DailyStepRecord?

    @Query("SELECT * FROM daily_step_records ORDER BY `date` DESC LIMIT 30")
    suspend fun getRecent30Days(): List<DailyStepRecord>
}
