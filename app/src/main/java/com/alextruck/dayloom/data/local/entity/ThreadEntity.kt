package com.alextruck.dayloom.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "threads")
data class ThreadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val timestamp: Long,
    val dateString: String, // yyyy-MM-dd
    val type: String, // EMOJI, WORD, TAG, VOICE, PHOTO
    val content: String,
    val promptQuestion: String,
    val note: String? = null
)
