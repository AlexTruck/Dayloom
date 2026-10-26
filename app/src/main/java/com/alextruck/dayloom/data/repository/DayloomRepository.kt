package com.alextruck.dayloom.data.repository

import com.alextruck.dayloom.data.local.dao.ThreadDao
import com.alextruck.dayloom.data.local.dao.WeaveDao
import com.alextruck.dayloom.data.local.entity.ThreadEntity
import com.alextruck.dayloom.data.local.entity.WeaveEntity
import com.alextruck.dayloom.domain.model.Thread
import com.alextruck.dayloom.domain.model.ThreadType
import com.alextruck.dayloom.domain.model.Weave
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DayloomRepository(
    private val threadDao: ThreadDao,
    private val weaveDao: WeaveDao
) {
    fun getThreadsForDate(dateString: String): Flow<List<Thread>> {
        return threadDao.getThreadsForDate(dateString).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun insertThread(thread: Thread): Long {
        return threadDao.insertThread(thread.toEntity())
    }

    fun getAllThreads(): Flow<List<Thread>> {
        return threadDao.getAllThreads().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getEchoThread(): Thread? {
        // ~30 days ago (between 29 and 31 days ago)
        val now = System.currentTimeMillis()
        val thirtyDaysMillis = 30L * 24 * 60 * 60 * 1000L
        val target = now - thirtyDaysMillis
        val min = target - 2L * 24 * 60 * 60 * 1000L
        val max = target + 2L * 24 * 60 * 60 * 1000L
        return threadDao.getRandomEchoThread(max, min)?.toDomain()
    }

    suspend fun getWeaveForDate(dateString: String): Weave? {
        return weaveDao.getWeaveForDate(dateString)?.toDomain()
    }

    fun getWeaveFlowForDate(dateString: String): Flow<Weave?> {
        return weaveDao.getWeaveFlowForDate(dateString).map { it?.toDomain() }
    }

    suspend fun saveWeave(weave: Weave) {
        weaveDao.insertWeave(weave.toEntity())
    }

    // Mappers
    private fun ThreadEntity.toDomain() = Thread(
        id = id,
        timestamp = timestamp,
        dateString = dateString,
        type = try { ThreadType.valueOf(type) } catch (e: Exception) { ThreadType.WORD },
        content = content,
        promptQuestion = promptQuestion,
        note = note
    )

    private fun Thread.toEntity() = ThreadEntity(
        id = id,
        timestamp = timestamp,
        dateString = dateString,
        type = type.name,
        content = content,
        promptQuestion = promptQuestion,
        note = note
    )

    private fun WeaveEntity.toDomain() = Weave(
        dateString = dateString,
        moodSummary = moodSummary,
        colorPaletteHex = colorPaletteHex.split(",").map { it.trim() }.filter { it.isNotEmpty() },
        keywords = keywords.split(",").map { it.trim() }.filter { it.isNotEmpty() },
        createdAt = createdAt
    )

    private fun Weave.toEntity() = WeaveEntity(
        dateString = dateString,
        moodSummary = moodSummary,
        colorPaletteHex = colorPaletteHex.joinToString(","),
        keywords = keywords.joinToString(","),
        createdAt = createdAt
    )
}
