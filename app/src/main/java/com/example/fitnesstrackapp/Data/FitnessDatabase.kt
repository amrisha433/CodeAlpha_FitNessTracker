package com.example.fitnesstrackapp.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Workout::class,
        HydrationEntry::class,
        DailyStepRecord::class,
        Achievement::class
    ],
    version = 4,
    exportSchema = false
)
abstract class FitnessDatabase : RoomDatabase() {

    abstract fun workoutDao(): WorkoutDao
    abstract fun hydrationDao(): HydrationDao
    abstract fun stepDao(): StepDao
    abstract fun achievementDao(): AchievementDao

    companion object {

        @Volatile
        private var INSTANCE: FitnessDatabase? = null

        fun getDatabase(context: Context): FitnessDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitnessDatabase::class.java,
                    "fitness_database"
                )
                    // Proper migration preserves all existing workout records.
                    .addMigrations(MIGRATION_3_4)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}