package com.alextruck.dayloom.util

import android.content.Context

object LanguageManager {
    private const val PREF_NAME = "dayloom_lang_prefs"
    private const val KEY_LAST_ROUTE = "last_route"

    fun saveLastRoute(context: Context, route: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LAST_ROUTE, route).apply()
    }

    fun consumeLastRoute(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val route = prefs.getString(KEY_LAST_ROUTE, "home") ?: "home"
        prefs.edit().remove(KEY_LAST_ROUTE).apply()
        return route
    }
}
