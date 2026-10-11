package com.example.fitnesstrackapp.Repository

import com.example.fitnesstrackapp.Data.Achievement
import com.example.fitnesstrackapp.Data.AchievementDao
import com.example.fitnesstrackapp.Data.AchievementDefinitions
import kotlinx.coroutines.flow.Flow

class AchievementRepository(private val dao: AchievementDao) {

    val achievements: Flow<List<Achievement>> = dao.observeAll()

    /**
     * Seeds the achievements table with all defined achievements if it's empty.
     * Uses IGNORE conflict strategy in the DAO so existing rows are never overwritten.
     */
    suspend fun seedIfEmpty() {
        dao.insertAll(AchievementDefinitions.ALL)
    }

    /**
     * Updates progress and, if [progress] >= [achievement.target] and the achievement
     * is not yet unlocked, records the unlock timestamp.
     */
    suspend fun updateProgress(id: String, progress: Int) {
        val existing = dao.getById(id) ?: return
        if (existing.isUnlocked) return  // never revoke an achievement
        val newUnlockedAt = if (progress >= existing.target) System.currentTimeMillis() else null
        dao.update(existing.copy(progress = progress, unlockedAt = newUnlockedAt))
    }

    /**
     * Unlocks an achievement immediately (for boolean achievements that don't use
     * a numeric progress counter).
     */
    suspend fun unlock(id: String) {
        val existing = dao.getById(id) ?: return
        if (existing.isUnlocked) return
        dao.update(existing.copy(unlockedAt = System.currentTimeMillis(), progress = existing.target))
    }
}
