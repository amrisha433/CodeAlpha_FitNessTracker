package com.example.fitnesstrackapp.Repository

import com.example.fitnesstrackapp.Data.Workout
import com.example.fitnesstrackapp.Data.WorkoutDao

class FitnessRepository(
    private val workoutDao: WorkoutDao
) {

    val workouts = workoutDao.getAllWorkouts()

    suspend fun insertWorkout(workout: Workout) {
        workoutDao.insertWorkout(workout)
    }

    suspend fun deleteWorkout(workout: Workout) {
        workoutDao.deleteWorkout(workout)
    }
}