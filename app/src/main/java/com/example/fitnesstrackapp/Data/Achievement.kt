package com.example.fitnesstrackapp.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a single achievement definition and its unlock state.
 *
 * [id]         Stable string key (e.g. "streak_7", "workouts_10"). Never reuse IDs.
 * [title]      Human-readable name shown in the UI.
 * [description] Achievement description shown in the UI.
 * [unlockedAt] UTC epoch ms when first unlocked; null means still locked.
 * [progress]   Numeric progress toward the achievement (e.g. current streak length).
 * [target]     The numeric threshold required to unlock.
 * [iconEmoji]  A single emoji used as the achievement icon.
 */
@Entity(tableName = "achievements")
data class Achievement(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val unlockedAt: Long? = null,
    val progress: Int = 0,
    val target: Int,
    val iconEmoji: String = "🏆"
) {
    val isUnlocked: Boolean get() = unlockedAt != null
}

/** All achievement IDs and their seed definitions. */
object AchievementDefinitions {
    val ALL = listOf(
        Achievement(id = "step_goal_first",    title = "First Steps",         description = "Reach your step goal for the first time",    target = 1,  iconEmoji = "👣"),
        Achievement(id = "hydration_goal_first",title = "Well Hydrated",      description = "Hit your hydration goal for the first time",  target = 1,  iconEmoji = "💧"),
        Achievement(id = "workout_goal_first",  title = "Goal Crusher",        description = "Complete your daily workout goal",             target = 1,  iconEmoji = "🎯"),
        Achievement(id = "streak_3",            title = "3-Day Streak",        description = "Work out 3 consecutive days",                 target = 3,  iconEmoji = "🔥"),
        Achievement(id = "streak_7",            title = "Week Warrior",        description = "Work out 7 consecutive days",                 target = 7,  iconEmoji = "⚡"),
        Achievement(id = "streak_14",           title = "Fortnight Fighter",   description = "Work out 14 consecutive days",                target = 14, iconEmoji = "💪"),
        Achievement(id = "streak_30",           title = "Iron Will",           description = "Work out 30 consecutive days",                target = 30, iconEmoji = "🏅"),
        Achievement(id = "workouts_10",         title = "10 Workouts",         description = "Log 10 workouts in total",                    target = 10, iconEmoji = "🏋️"),
        Achievement(id = "workouts_25",         title = "25 Workouts",         description = "Log 25 workouts in total",                    target = 25, iconEmoji = "🌟"),
        Achievement(id = "workouts_50",         title = "50 Workouts",         description = "Log 50 workouts in total",                    target = 50, iconEmoji = "🏆"),
        Achievement(id = "calories_5000",       title = "5,000 kcal",          description = "Reach 5,000 recorded workout calories",       target = 5000, iconEmoji = "🔥"),
        Achievement(id = "hydration_7days",     title = "Hydration Habit",     description = "Meet your water goal 7 different days",       target = 7,  iconEmoji = "💦"),
    )
}
