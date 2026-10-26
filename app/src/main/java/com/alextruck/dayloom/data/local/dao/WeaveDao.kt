package com.alextruck.dayloom.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alextruck.dayloom.data.local.entity.WeaveEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeaveDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeave(weave: WeaveEntity)

    @Query("SELECT * FROM weaves WHERE dateString = :dateString")
    suspend fun getWeaveForDate(dateString: String): WeaveEntity?

    @Query("SELECT * FROM weaves WHERE dateString = :dateString")
    fun getWeaveFlowForDate(dateString: String): Flow<WeaveEntity?>

    @Query("SELECT * FROM weaves ORDER BY dateString DESC")
    fun getAllWeaves(): Flow<List<WeaveEntity>>
}
