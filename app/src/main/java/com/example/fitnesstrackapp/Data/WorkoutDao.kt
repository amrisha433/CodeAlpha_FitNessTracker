package com.example.fitnesstrackapp.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Insert
    suspend fun insertWorkout(workout: Workout)

    @Update
    suspend fun updateWorkout(workout: Workout)

    @Delete
    suspend fun deleteWorkout(workout: Workout)

    /** All workouts, newest first – used by History screen. */
    @Query("SELECT * FROM workouts ORDER BY date DESC")
    fun getAllWorkouts(): Flow<List<Workout>>

    /** Workouts with a [date] timestamp within the given range (epoch ms, inclusive). */
    @Query("SELECT * FROM workouts WHERE date >= :startMs AND date <= :endMs ORDER BY date DESC")
    fun getWorkoutsBetween(startMs: Long, endMs: Long): Flow<List<Workout>>

    /** Distinct calendar-day dates (epoch ms) that have at least one workout. */
    @Query("SELECT DISTINCT date FROM workouts ORDER BY date ASC")
    suspend fun getAllWorkoutDates(): List<Long>
}