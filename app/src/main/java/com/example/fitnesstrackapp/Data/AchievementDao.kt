package com.example.fitnesstrackapp.Data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(achievements: List<Achievement>)

    @Update
    suspend fun update(achievement: Achievement)

    @Query("SELECT * FROM achievements ORDER BY unlockedAt DESC NULLS LAST, id ASC")
    fun observeAll(): Flow<List<Achievement>>

    @Query("SELECT * FROM achievements WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): Achievement?

    @Query("SELECT COUNT(*) FROM achievements")
    suspend fun count(): Int
}
