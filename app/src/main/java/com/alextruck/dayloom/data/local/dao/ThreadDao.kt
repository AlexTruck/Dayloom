package com.alextruck.dayloom.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alextruck.dayloom.data.local.entity.ThreadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ThreadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThread(thread: ThreadEntity): Long

    @Query("SELECT * FROM threads WHERE dateString = :dateString ORDER BY timestamp ASC")
    fun getThreadsForDate(dateString: String): Flow<List<ThreadEntity>>

    @Query("SELECT * FROM threads WHERE dateString = :dateString ORDER BY timestamp ASC")
    suspend fun getThreadsForDateSync(dateString: String): List<ThreadEntity>

    @Query("SELECT * FROM threads WHERE timestamp <= :targetTimestamp AND timestamp >= :minTimestamp ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomEchoThread(targetTimestamp: Long, minTimestamp: Long): ThreadEntity?

    @Query("SELECT * FROM threads ORDER BY timestamp DESC")
    fun getAllThreads(): Flow<List<ThreadEntity>>

    @Query("SELECT * FROM threads WHERE id = :id")
    suspend fun getThreadById(id: Long): ThreadEntity?
}
