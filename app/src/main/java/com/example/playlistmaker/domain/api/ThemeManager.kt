package com.example.playlistmaker.domain.api

import android.content.Context

interface ThemeManager {
    fun setDarkTheme(enabled: Boolean, context: Context)
    fun isDarkThemeEnabled(context: Context): Boolean
}