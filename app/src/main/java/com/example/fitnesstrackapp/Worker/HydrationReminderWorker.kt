package com.example.fitnesstrackapp.Worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.fitnesstrackapp.Data.FitnessDatabase
import com.example.fitnesstrackapp.Preferences.GoalPreferences
import com.example.fitnesstrackapp.Preferences.ReminderPreferences
import com.example.fitnesstrackapp.Repository.HydrationRepository
import kotlinx.coroutines.flow.first
import java.time.LocalTime
import java.util.Calendar

/**
 * WorkManager worker that checks whether the user should receive a hydration reminder.
 *
 * Execution flow:
 * 1. Read reminder settings from DataStore.
 * 2. If reminders are disabled → do nothing.
 * 3. Check whether current local time is within the configured window → do nothing if outside.
 * 4. Compute today's total water intake from Room.
 * 5. If today's total >= goal → do nothing (goal already met).
 * 6. Post a notification via NotificationManagerCompat.
 *
 * Note: WorkManager guarantees execution but may delay by up to ~15 minutes on Doze-mode
 * devices. This is expected behaviour and is disclosed in the UI.
 */
class HydrationReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val CHANNEL_ID = "hydration_reminder_channel"
        const val NOTIFICATION_ID = 1001
        private const val TAG = "HydrationReminderWorker"
    }

    override suspend fun doWork(): Result {
        createNotificationChannel()

        // 1. Read settings
        val reminderPrefs = ReminderPreferences(applicationContext)
        val settings = reminderPrefs.settings.first()
        if (!settings.enabled) {
            Log.d(TAG, "Reminders disabled – skipping")
            return Result.success()
        }

        // 2. Check time window
        val now = LocalTime.now()
        val windowStart = LocalTime.of(settings.windowStartHour, 0)
        val windowEnd   = LocalTime.of(settings.windowEndHour, 0)
        val inWindow = if (windowStart <= windowEnd) {
            now >= windowStart && now < windowEnd
        } else {
            now >= windowStart || now < windowEnd  // overnight window
        }
        if (!inWindow) {
            Log.d(TAG, "Outside reminder window ($now) – skipping")
            return Result.success()
        }

        // 3. Check daily total vs goal
        val goalPrefs = GoalPreferences(applicationContext)
        val goals = goalPrefs.goals.first()

        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0);      cal.set(Calendar.MILLISECOND, 0)
        val dayStartMs = cal.timeInMillis
        cal.set(Calendar.HOUR_OF_DAY, 23); cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59);      cal.set(Calendar.MILLISECOND, 999)
        val dayEndMs = cal.timeInMillis

        val db = FitnessDatabase.getDatabase(applicationContext)
        val hydrationRepo = HydrationRepository(db.hydrationDao())
        val totalMl = hydrationRepo.getDailyTotalMl(dayStartMs, dayEndMs).first()

        if (totalMl >= goals.waterGoalMl) {
            Log.d(TAG, "Daily water goal already met (${totalMl}ml) – skipping notification")
            return Result.success()
        }

        // 4. Post notification
        val remaining = goals.waterGoalMl - totalMl
        postNotification(totalMl, goals.waterGoalMl, remaining)
        return Result.success()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Hydration Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Periodic reminders to drink water"
            }
            val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }

    private fun postNotification(currentMl: Int, goalMl: Int, remainingMl: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.w(TAG, "POST_NOTIFICATIONS permission not granted – cannot show reminder")
                return
            }
        }

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("💧 Time to Hydrate!")
            .setContentText("${currentMl}ml / ${goalMl}ml today. Drink ${remainingMl}ml more!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, notification)
    }
}
