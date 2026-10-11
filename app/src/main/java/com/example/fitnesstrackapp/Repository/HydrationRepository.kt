package com.example.fitnesstrackapp.Repository

import com.example.fitnesstrackapp.Data.HydrationDao
import com.example.fitnesstrackapp.Data.HydrationEntry
import kotlinx.coroutines.flow.Flow

class HydrationRepository(private val dao: HydrationDao) {

    /** Inserts a new hydration record. Never mutates existing records. */
    suspend fun addEntry(amountMl: Int, timestamp: Long = System.currentTimeMillis()) {
        dao.insert(HydrationEntry(amountMl = amountMl, timestamp = timestamp))
    }

    /** Deletes a specific record (used for undo). */
    suspend fun removeEntry(entry: HydrationEntry) = dao.delete(entry)

    /**
     * Observes all entries within a date boundary as a reactive Flow.
     * [startMs] and [endMs] should be the start and end of the calendar day in local time.
     */
    fun getEntriesToday(startMs: Long, endMs: Long): Flow<List<HydrationEntry>> =
        dao.getEntriesBetween(startMs, endMs)

    /** Reactive total ml consumed between the given timestamps. */
    fun getDailyTotalMl(startMs: Long, endMs: Long): Flow<Int> =
        dao.getTotalMlBetween(startMs, endMs)

    /** Historical entries for analytics. */
    fun getAllEntries(): Flow<List<HydrationEntry>> = dao.getAllEntries()

    /** Count of calendar days where the user met their goal. Non-reactive; call from coroutine. */
    suspend fun countDaysMeetingGoal(goalMl: Int): Int = dao.countDaysMeetingGoal(goalMl)
}
