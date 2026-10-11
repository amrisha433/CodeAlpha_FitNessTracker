package com.example.fitnesstrackapp.Preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Extension property gives one DataStore per app process. */
private val Context.goalDataStore: DataStore<Preferences> by preferencesDataStore(name = "goal_preferences")

/**
 * Persistent goal preferences backed by DataStore.
 *
 * All values are validated on write; invalid/negative values are clamped to
 * the minimum sensible value. Reads always return a non-null default.
 */
class GoalPreferences(private val context: Context) {

    companion object {
        private val KEY_STEPS = intPreferencesKey("goal_steps")
        private val KEY_WATER_ML = intPreferencesKey("goal_water_ml")
        private val KEY_WORKOUT_DURATION = intPreferencesKey("goal_workout_duration_min")
        private val KEY_WORKOUT_COUNT = intPreferencesKey("goal_workout_count")
        private val KEY_CALORIES = intPreferencesKey("goal_calories")

        // Reasonable defaults
        const val DEFAULT_STEPS = 10_000
        const val DEFAULT_WATER_ML = 2000        // 2 litres
        const val DEFAULT_WORKOUT_DURATION = 30  // minutes per day
        const val DEFAULT_WORKOUT_COUNT = 3
        const val DEFAULT_CALORIES = 500         // kcal via exercise per day

        // Validation ranges
        const val MIN_STEPS = 500;   const val MAX_STEPS = 100_000
        const val MIN_WATER = 250;   const val MAX_WATER = 10_000  // ml
        const val MIN_DURATION = 5;  const val MAX_DURATION = 480  // minutes
        const val MIN_WORKOUT_COUNT = 1; const val MAX_WORKOUT_COUNT = 20
        const val MIN_CALORIES = 50; const val MAX_CALORIES = 5_000
    }

    data class Goals(
        val stepGoal: Int = DEFAULT_STEPS,
        val waterGoalMl: Int = DEFAULT_WATER_ML,
        val workoutDurationGoalMin: Int = DEFAULT_WORKOUT_DURATION,
        val workoutCountGoal: Int = DEFAULT_WORKOUT_COUNT,
        val calorieGoal: Int = DEFAULT_CALORIES
    )

    val goals: Flow<Goals> = context.goalDataStore.data.map { prefs ->
        Goals(
            stepGoal               = prefs[KEY_STEPS]            ?: DEFAULT_STEPS,
            waterGoalMl            = prefs[KEY_WATER_ML]          ?: DEFAULT_WATER_ML,
            workoutDurationGoalMin = prefs[KEY_WORKOUT_DURATION]  ?: DEFAULT_WORKOUT_DURATION,
            workoutCountGoal       = prefs[KEY_WORKOUT_COUNT]     ?: DEFAULT_WORKOUT_COUNT,
            calorieGoal            = prefs[KEY_CALORIES]          ?: DEFAULT_CALORIES
        )
    }

    /** Validates and saves all goal fields in a single transaction. */
    suspend fun saveGoals(goals: Goals) {
        context.goalDataStore.edit { prefs ->
            prefs[KEY_STEPS]           = goals.stepGoal.coerceIn(MIN_STEPS, MAX_STEPS)
            prefs[KEY_WATER_ML]        = goals.waterGoalMl.coerceIn(MIN_WATER, MAX_WATER)
            prefs[KEY_WORKOUT_DURATION]= goals.workoutDurationGoalMin.coerceIn(MIN_DURATION, MAX_DURATION)
            prefs[KEY_WORKOUT_COUNT]   = goals.workoutCountGoal.coerceIn(MIN_WORKOUT_COUNT, MAX_WORKOUT_COUNT)
            prefs[KEY_CALORIES]        = goals.calorieGoal.coerceIn(MIN_CALORIES, MAX_CALORIES)
        }
    }
}
