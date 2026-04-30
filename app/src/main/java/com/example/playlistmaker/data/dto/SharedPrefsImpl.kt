package com.example.playlistmaker.data.dto

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import com.example.playlistmaker.domain.api.SharedPrefs
import com.example.playlistmaker.domain.api.ThemeManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPrefsImpl : SharedPrefs, ThemeManager {
    companion object {
        const val APP_PREFERENCES = "AppPreferences"
        const val DARK_THEME_KEY = "dark_theme"
        const val HISTORY_KEY = "search_history_key"
    }
    private val gson= Gson()

    override fun saveHistory(history: List<TrackDto>, context: Context) {
        val sharedPrefs = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        val json = gson.toJson(history)
        sharedPrefs.edit {
            putString(HISTORY_KEY, json)
        }
    }

    override fun getHistory(context: Context): MutableList<TrackDto> {
        val sharedPrefs = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        val json = sharedPrefs.getString(HISTORY_KEY, null)
        var list: MutableList<TrackDto> = mutableListOf()
        if (json != null) {
            val type = object : TypeToken<MutableList<TrackDto>>() {}.type
            list = gson.fromJson(json, type)
            return list
        } else {
            return list
        }
    }

    override fun setDarkTheme(enabled: Boolean, context: Context) {
        val sharedPrefs = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        sharedPrefs.edit {
            putBoolean(DARK_THEME_KEY, enabled)
        }
    }

    override fun isDarkThemeEnabled(context: Context): Boolean {
        val sharedPrefs = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        return sharedPrefs.getBoolean(DARK_THEME_KEY, false)
    }
}