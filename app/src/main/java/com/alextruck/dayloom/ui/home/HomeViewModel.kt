package com.alextruck.dayloom.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alextruck.dayloom.data.local.DayloomDatabase
import com.alextruck.dayloom.data.repository.DayloomRepository
import com.alextruck.dayloom.domain.model.ReflectivePrompt
import com.alextruck.dayloom.domain.model.Thread
import com.alextruck.dayloom.domain.model.ThreadType
import com.alextruck.dayloom.domain.model.Weave
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val database = DayloomDatabase.getDatabase(application)
    private val repository = DayloomRepository(database.threadDao(), database.weaveDao())

    private val currentDateString: String
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    val todayThreads: StateFlow<List<Thread>> = repository.getThreadsForDate(currentDateString)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todayWeave: StateFlow<Weave?> = repository.getWeaveFlowForDate(currentDateString)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val echoThread: StateFlow<Thread?> = flow {
        emit(repository.getEchoThread())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val currentPrompt: String
        get() = ReflectivePrompt.random()

    fun addThread(type: ThreadType, content: String, prompt: String, note: String? = null) {
        viewModelScope.launch {
            val thread = Thread(
                timestamp = System.currentTimeMillis(),
                dateString = currentDateString,
                type = type,
                content = content,
                promptQuestion = prompt,
                note = note
            )
            repository.insertThread(thread)
            checkAndGenerateWeave()
        }
    }

    private suspend fun checkAndGenerateWeave() {
        val threads = threadDaoSync(currentDateString)
        if (threads.size >= 2 && repository.getWeaveForDate(currentDateString) == null) {
            val emojis = threads.filter { it.type == ThreadType.EMOJI }.map { it.content }
            val words = threads.filter { it.type == ThreadType.WORD || it.type == ThreadType.TAG }.map { it.content }
            val summary = if (words.isNotEmpty()) "A day woven with ${words.joinToString(", ")}." else "A quiet, reflective day."
            val palette = listOf("#C86D51", "#7A8B79", "#F3EFEA")
            val weave = Weave(
                dateString = currentDateString,
                moodSummary = summary,
                colorPaletteHex = palette,
                keywords = words.take(4),
                createdAt = System.currentTimeMillis()
            )
            repository.saveWeave(weave)
        }
    }

    private suspend fun threadDaoSync(dateStr: String): List<Thread> {
        return database.threadDao().getThreadsForDateSync(dateStr).map {
            Thread(it.id, it.timestamp, it.dateString, ThreadType.valueOf(it.type), it.content, it.promptQuestion, it.note)
        }
    }
}
