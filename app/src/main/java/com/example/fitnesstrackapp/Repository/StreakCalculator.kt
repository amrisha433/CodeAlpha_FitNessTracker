package com.example.fitnesstrackapp.Repository

import java.time.LocalDate
import java.time.ZoneId

/**
 * Pure business-logic object for streak calculation.
 *
 * All methods are stateless and side-effect free, making them straightforward to unit-test.
 */
object StreakCalculator {

    /**
     * Calculates the current consecutive-day workout streak.
     *
     * @param workoutTimestampsMs   All workout timestamps (epoch ms) from the database.
     * @param today                 The reference date to count back from (defaults to today).
     * @param zone                  The device's local timezone.
     *
     * Rules:
     * - Each calendar day counts once regardless of how many workouts occurred.
     * - The streak is broken if any calendar day (starting from [today] and going backwards)
     *   has no workout. A missing day resets the count.
     * - Today is included only if it has at least one workout.
     * - Tomorrow's workouts are ignored.
     */
    fun currentStreak(
        workoutTimestampsMs: List<Long>,
        today: LocalDate = LocalDate.now(),
        zone: ZoneId = ZoneId.systemDefault()
    ): Int {
        if (workoutTimestampsMs.isEmpty()) return 0

        // Convert timestamps to a set of local calendar dates.
        val activeDays: Set<LocalDate> = workoutTimestampsMs
            .map { ms -> LocalDate.ofInstant(java.time.Instant.ofEpochMilli(ms), zone) }
            .toSet()

        // Walk backwards from today. Stop when a day with no workout is found.
        var streak = 0
        var cursor = today
        while (activeDays.contains(cursor)) {
            streak++
            cursor = cursor.minusDays(1)
        }
        return streak
    }

    /**
     * Returns the longest-ever consecutive streak from the given workout timestamps.
     * Used for all-time personal best display.
     */
    fun longestStreak(
        workoutTimestampsMs: List<Long>,
        zone: ZoneId = ZoneId.systemDefault()
    ): Int {
        if (workoutTimestampsMs.isEmpty()) return 0

        val activeDays = workoutTimestampsMs
            .map { ms -> LocalDate.ofInstant(java.time.Instant.ofEpochMilli(ms), zone) }
            .toSortedSet()

        var longest = 0
        var current = 0
        var previous: LocalDate? = null

        for (day in activeDays) {
            if (previous == null || day == previous!!.plusDays(1)) {
                current++
            } else {
                current = 1
            }
            if (current > longest) longest = current
            previous = day
        }
        return longest
    }
}
