package com.alextruck.dayloom.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weaves")
data class WeaveEntity(
    @PrimaryKey
    val dateString: String, // yyyy-MM-dd
    val moodSummary: String,
    val colorPaletteHex: String, // comma separated hex colors
    val keywords: String, // comma separated keywords
    val createdAt: Long
)
