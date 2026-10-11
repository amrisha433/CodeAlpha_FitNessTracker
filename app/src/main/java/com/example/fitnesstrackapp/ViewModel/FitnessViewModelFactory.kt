package com.example.fitnesstrackapp.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitnesstrackapp.Data.FitnessDatabase
import com.example.fitnesstrackapp.Preferences.GoalPreferences
import com.example.fitnesstrackapp.Preferences.ReminderPreferences
import com.example.fitnesstrackapp.Preferences.StepPreferences
import com.example.fitnesstrackapp.Repository.AchievementRepository
import com.example.fitnesstrackapp.Repository.FitnessRepository
import com.example.fitnesstrackapp.Repository.HydrationRepository

class FitnessViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FitnessViewModel::class.java)) {
            val db = FitnessDatabase.getDatabase(context)

            val fitnessRepo    = FitnessRepository(db.workoutDao())
            val hydrationRepo  = HydrationRepository(db.hydrationDao())
            val achievementRepo = AchievementRepository(db.achievementDao())
            val goalPrefs      = GoalPreferences(context)
            val stepPrefs      = StepPreferences(context)
            val reminderPrefs  = ReminderPreferences(context)

            @Suppress("UNCHECKED_CAST")
            return FitnessViewModel(
                repository          = fitnessRepo,
                hydrationRepository = hydrationRepo,
                achievementRepository = achievementRepo,
                goalPreferences     = goalPrefs,
                stepPreferences     = stepPrefs,
                reminderPreferences = reminderPrefs,
                appContext          = context.applicationContext
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}