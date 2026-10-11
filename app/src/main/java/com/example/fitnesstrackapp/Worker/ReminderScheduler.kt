package com.example.fitnesstrackapp.Worker

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.fitnesstrackapp.Preferences.ReminderPreferences
import java.util.concurrent.TimeUnit

/**
 * Manages scheduling and cancellation of the periodic hydration reminder.
 *
 * Uses a uniquely-named periodic WorkRequest so duplicate enqueues replace
 * the existing chain rather than stacking multiple workers.
 */
object ReminderScheduler {

    private const val WORK_NAME = "hydration_reminder_periodic"
    private const val TAG = "ReminderScheduler"

    /**
     * Schedules or cancels the periodic reminder based on [settings].
     * Safe to call multiple times – existing work is replaced, not duplicated.
     */
    fun applySettings(context: Context, settings: ReminderPreferences.ReminderSettings) {
        val workManager = WorkManager.getInstance(context)

        if (!settings.enabled) {
            workManager.cancelUniqueWork(WORK_NAME)
            Log.d(TAG, "Hydration reminders cancelled")
            return
        }

        val intervalHours = settings.intervalHours.toLong().coerceAtLeast(1L)

        val request = PeriodicWorkRequestBuilder<HydrationReminderWorker>(
            intervalHours, TimeUnit.HOURS
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(false)
                    .build()
            )
            .addTag(WORK_NAME)
            .build()

        workManager.enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,  // replaces existing on settings change
            request
        )

        Log.d(TAG, "Hydration reminder scheduled every ${intervalHours}h")
    }

    /** Cancels all reminder work unconditionally. */
    fun cancel(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        Log.d(TAG, "Hydration reminders cancelled (explicit)")
    }
}
