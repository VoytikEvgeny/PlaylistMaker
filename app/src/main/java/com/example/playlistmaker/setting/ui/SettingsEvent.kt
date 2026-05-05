package com.example.playlistmaker.setting.ui

import android.content.Intent

sealed class SettingsEvent {
    data class Event(val intent: Intent, val errorMessage: String) : SettingsEvent()
    data class Theme(val isDark: Boolean) : SettingsEvent()
}