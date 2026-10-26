package com.alextruck.dayloom.domain.model

data class Thread(
    val id: Long = 0L,
    val timestamp: Long,
    val dateString: String,
    val type: ThreadType,
    val content: String,
    val promptQuestion: String,
    val note: String? = null
)
