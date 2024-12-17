package com.example.listapp

import android.content.Context

object ThemeManager {
    private const val PREFS_NAME = "ThemePrefs"
    private const val KEY_THEME = "app_theme"

    const val THEME_LIGHT = "Light"
    const val THEME_DARK = "Dark"

    fun saveTheme(context: Context, theme: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_THEME, theme).apply()
    }

    fun loadTheme(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_THEME, THEME_LIGHT) ?: THEME_LIGHT
    }

    fun applyTheme(context: Context) {
        when (loadTheme(context)) {
            THEME_DARK -> context.setTheme(R.style.AppTheme_Dark)
            else -> context.setTheme(R.style.AppTheme_Light)
        }
    }
}