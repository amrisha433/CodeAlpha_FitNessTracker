package com.example.fitnesstrackapp.Data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HydrationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: HydrationEntry)

    @Delete
    suspend fun delete(entry: HydrationEntry)

    /** Returns all entries whose [timestamp] falls within [startMs]..[endMs] (inclusive). */
    @Query("SELECT * FROM hydration_entries WHERE timestamp >= :startMs AND timestamp <= :endMs ORDER BY timestamp DESC")
    fun getEntriesBetween(startMs: Long, endMs: Long): Flow<List<HydrationEntry>>

    /** Sum of amountMl for entries on today's date boundaries. */
    @Query("SELECT COALESCE(SUM(amountMl), 0) FROM hydration_entries WHERE timestamp >= :startMs AND timestamp <= :endMs")
    fun getTotalMlBetween(startMs: Long, endMs: Long): Flow<Int>

    /** All entries – used for historical analytics. */
    @Query("SELECT * FROM hydration_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<HydrationEntry>>

    /** Count of distinct calendar dates (in UTC) where sum >= goalMl. Used for 7-day achievement. */
    @Query("""
        SELECT COUNT(DISTINCT date(timestamp / 1000, 'unixepoch')) 
        FROM hydration_entries 
        GROUP BY date(timestamp / 1000, 'unixepoch')
        HAVING SUM(amountMl) >= :goalMl
    """)
    suspend fun countDaysMeetingGoal(goalMl: Int): Int
}
