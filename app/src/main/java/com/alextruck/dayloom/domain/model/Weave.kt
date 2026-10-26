package com.alextruck.dayloom.domain.model

data class Weave(
    val dateString: String,
    val moodSummary: String,
    val colorPaletteHex: List<String>,
    val keywords: List<String>,
    val createdAt: Long
)
