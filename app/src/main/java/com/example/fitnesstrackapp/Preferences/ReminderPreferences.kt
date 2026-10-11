package com.example.fitnesstrackapp.Preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.reminderDataStore: DataStore<Preferences> by preferencesDataStore(name = "reminder_preferences")

/**
 * Persistent reminder configuration stored in DataStore.
 *
 * [enabled]               Whether hydration reminders are active.
 * [intervalHours]         How often to check (WorkManager minimum is 15 min; we offer 1+ hours).
 * [windowStartHour]       Hour of day (0-23) when reminders may start.
 * [windowEndHour]         Hour of day (0-23) after which reminders stop.
 */
class ReminderPreferences(private val context: Context) {

    companion object {
        private val KEY_ENABLED       = booleanPreferencesKey("reminder_enabled")
        private val KEY_INTERVAL      = intPreferencesKey("reminder_interval_hours")
        private val KEY_WINDOW_START  = intPreferencesKey("reminder_window_start")
        private val KEY_WINDOW_END    = intPreferencesKey("reminder_window_end")

        const val DEFAULT_ENABLED      = false
        const val DEFAULT_INTERVAL     = 2    // hours
        const val DEFAULT_WINDOW_START = 8    // 08:00
        const val DEFAULT_WINDOW_END   = 22   // 22:00
    }

    data class ReminderSettings(
        val enabled: Boolean = DEFAULT_ENABLED,
        val intervalHours: Int = DEFAULT_INTERVAL,
        val windowStartHour: Int = DEFAULT_WINDOW_START,
        val windowEndHour: Int = DEFAULT_WINDOW_END
    )

    val settings: Flow<ReminderSettings> = context.reminderDataStore.data.map { prefs ->
        ReminderSettings(
            enabled        = prefs[KEY_ENABLED]      ?: DEFAULT_ENABLED,
            intervalHours  = prefs[KEY_INTERVAL]     ?: DEFAULT_INTERVAL,
            windowStartHour= prefs[KEY_WINDOW_START] ?: DEFAULT_WINDOW_START,
            windowEndHour  = prefs[KEY_WINDOW_END]   ?: DEFAULT_WINDOW_END
        )
    }

    suspend fun saveSettings(settings: ReminderSettings) {
        context.reminderDataStore.edit { prefs ->
            prefs[KEY_ENABLED]      = settings.enabled
            prefs[KEY_INTERVAL]     = settings.intervalHours.coerceIn(1, 24)
            prefs[KEY_WINDOW_START] = settings.windowStartHour.coerceIn(0, 23)
            prefs[KEY_WINDOW_END]   = settings.windowEndHour.coerceIn(0, 23)
        }
    }
}
