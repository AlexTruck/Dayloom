package com.alextruck.dayloom.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "echo_interactions")
data class EchoInteractionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val originalThreadId: Long,
    val reflectionContent: String?,
    val interactedAt: Long
)
