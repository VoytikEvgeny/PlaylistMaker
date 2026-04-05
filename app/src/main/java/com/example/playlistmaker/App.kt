package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

const val APPLICATION_PREFERENCES="Application_preferences"
const val DARK_THEME_KEY="theme_preferences"
class App: Application() {
    private var darkTheme=false
    private var sharedPrefs : SharedPreferences? = null

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(APPLICATION_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs!!.getBoolean(DARK_THEME_KEY, false)
        switchTheme(darkTheme)
    }
    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPrefs?.edit {
            putBoolean(DARK_THEME_KEY, darkTheme)
        }
    }

}