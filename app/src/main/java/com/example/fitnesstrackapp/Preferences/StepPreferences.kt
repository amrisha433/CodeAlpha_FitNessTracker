package com.example.fitnesstrackapp.Preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

/** One DataStore per process. */
private val Context.stepDataStore: DataStore<Preferences> by preferencesDataStore(name = "step_preferences")

/**
 * Persists the sensor baseline information needed to calculate today's step count correctly
 * across app restarts and device reboots.
 *
 * [lastKnownDate]        The calendar date (yyyy-MM-dd) for which the baseline was recorded.
 * [cumulativeAtDayStart] The TYPE_STEP_COUNTER value at the moment [lastKnownDate] began.
 *                        Steps for today = currentSensorValue - cumulativeAtDayStart  (when dates match).
 * [todaySteps]           The most recently computed today-steps value (cached for display between sensor events).
 */
class StepPreferences(private val context: Context) {

    companion object {
        private val KEY_DATE = stringPreferencesKey("step_baseline_date")
        private val KEY_CUMULATIVE_AT_DAY_START = longPreferencesKey("step_cumulative_at_day_start")
        private val KEY_TODAY_STEPS = longPreferencesKey("step_today_cached")
    }

    data class StepBaseline(
        val date: String = "",
        val cumulativeAtDayStart: Long = 0L,
        val todaySteps: Int = 0
    )

    suspend fun read(): StepBaseline {
        val prefs = context.stepDataStore.data.first()
        return StepBaseline(
            date                 = prefs[KEY_DATE]                   ?: "",
            cumulativeAtDayStart = prefs[KEY_CUMULATIVE_AT_DAY_START] ?: 0L,
            todaySteps           = (prefs[KEY_TODAY_STEPS]            ?: 0L).toInt()
        )
    }

    suspend fun save(baseline: StepBaseline) {
        context.stepDataStore.edit { prefs ->
            prefs[KEY_DATE]                   = baseline.date
            prefs[KEY_CUMULATIVE_AT_DAY_START] = baseline.cumulativeAtDayStart
            prefs[KEY_TODAY_STEPS]             = baseline.todaySteps.toLong()
        }
    }
}
