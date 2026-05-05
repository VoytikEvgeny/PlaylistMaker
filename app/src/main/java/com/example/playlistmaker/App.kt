package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.creator.Creator

const val HISTORY_LIST_SIZE = 10

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        val interactor = Creator.provideSettingsInteractor()
        interactor.updateThemeSetting(interactor.getThemeSettings())
    }
}
