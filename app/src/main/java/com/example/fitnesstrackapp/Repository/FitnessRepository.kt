package com.example.fitnesstrackapp.Repository

import com.example.fitnesstrackapp.Data.Workout
import com.example.fitnesstrackapp.Data.WorkoutDao
import kotlinx.coroutines.flow.Flow

class FitnessRepository(
    private val workoutDao: WorkoutDao
) {
    val workouts: Flow<List<Workout>> = workoutDao.getAllWorkouts()

    suspend fun insertWorkout(workout: Workout) = workoutDao.insertWorkout(workout)

    suspend fun updateWorkout(workout: Workout) = workoutDao.updateWorkout(workout)

    suspend fun deleteWorkout(workout: Workout) = workoutDao.deleteWorkout(workout)

    fun getWorkoutsBetween(startMs: Long, endMs: Long): Flow<List<Workout>> =
        workoutDao.getWorkoutsBetween(startMs, endMs)

    suspend fun getAllWorkoutDates(): List<Long> = workoutDao.getAllWorkoutDates()
}