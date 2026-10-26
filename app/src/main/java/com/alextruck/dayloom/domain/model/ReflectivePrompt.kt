package com.alextruck.dayloom.domain.model

import java.util.Locale

enum class ReflectivePrompt(val questionEn: String, val questionDe: String) {
    MORNING_ENERGY(
        "What word describes your morning energy?",
        "Welches Wort beschreibt deine Morgenenergie?"
    ),
    MOMENT_PAUSE(
        "Which moment made you pause today?",
        "Welcher Moment hat dich heute innehalten lassen?"
    ),
    SMALL_DETAIL(
        "What is a small detail you want to remember from today?",
        "Was ist ein kleines Detail von heute, an das du dich erinnern möchtest?"
    ),
    HEART_FEELING(
        "How is your heart feeling this evening?",
        "Wie fühlt sich dein Herz heute Abend an?"
    );

    companion object {
        fun random(): String {
            val isGerman = Locale.getDefault().language.equals("de", ignoreCase = true)
            val prompt = entries.random()
            return if (isGerman) prompt.questionDe else prompt.questionEn
        }
    }
}
