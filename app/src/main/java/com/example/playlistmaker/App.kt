package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

const val HISTORY_LIST_SIZE = 10
const val I_TUNES_BASE_URL = "https://itunes.apple.com"
const val SEARCH_DEBOUNCE_DELAY = 2000L
const val CLICKED_TRACK_CONTENT = "clicked_track"
const val TRACK_HISTORY = "track_history"
const val APPLICATION_PREFERENCES = "Application_preferences"
const val DARK_THEME_KEY = "theme_preferences"

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
    }
}
