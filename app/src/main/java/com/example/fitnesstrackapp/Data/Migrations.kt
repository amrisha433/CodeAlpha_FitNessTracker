package com.example.fitnesstrackapp.Data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Migration from version 3 to 4.
 *
 * Creates three new tables:
 *   - hydration_entries  : per-entry water intake records with timestamps
 *   - daily_step_records : one row per calendar date, sensor-derived step counts
 *   - achievements       : achievement state (locked/unlocked + progress)
 *
 * The existing 'workouts' table schema is unchanged, so all existing workout
 * records are fully preserved.
 */
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `hydration_entries` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `amountMl` INTEGER NOT NULL,
                `timestamp` INTEGER NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `daily_step_records` (
                `date` TEXT PRIMARY KEY NOT NULL,
                `steps` INTEGER NOT NULL,
                `cumulativeAtDayStart` INTEGER NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `achievements` (
                `id` TEXT PRIMARY KEY NOT NULL,
                `title` TEXT NOT NULL,
                `description` TEXT NOT NULL,
                `unlockedAt` INTEGER,
                `progress` INTEGER NOT NULL DEFAULT 0,
                `target` INTEGER NOT NULL,
                `iconEmoji` TEXT NOT NULL DEFAULT '🏆'
            )
        """.trimIndent())
    }
}
