package com.example.playlistmaker.setting.data

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.example.playlistmaker.DARK_THEME_KEY
import com.example.playlistmaker.setting.domain.SettingsRepository
import com.example.playlistmaker.setting.domain.ThemeSettings

class SettingsRepositoryImpl(private val prefs: SharedPreferences, private val context: Context) :
    SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(prefs.getBoolean(DARK_THEME_KEY, false))
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        AppCompatDelegate.setDefaultNightMode(
            if (settings.isDark) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        prefs.edit {
            putBoolean(DARK_THEME_KEY, settings.isDark)
        }
    }

}