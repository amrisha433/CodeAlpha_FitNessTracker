package com.example.fitnesstrackapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstrackapp.Data.Workout
import com.example.fitnesstrackapp.Repository.FitnessRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FitnessViewModel(
    private val repository: FitnessRepository
) : ViewModel() {

    val totalWorkouts
        get() = workouts.value.size

    val totalCalories
        get() = workouts.value.sumOf { it.calories }

    val totalDuration
        get() = workouts.value.sumOf { it.duration }

    val workouts = repository.workouts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addWorkout(
        name: String,
        duration: Int,
        calories: Int,
        category: String
    ) {
        viewModelScope.launch {
            repository.insertWorkout(
                Workout(
                    name = name,
                    duration = duration,
                    category = category,
                    calories = calories,
                    date = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteWorkout(workout: Workout) {
        viewModelScope.launch {
            repository.deleteWorkout(workout)
        }
    }
}