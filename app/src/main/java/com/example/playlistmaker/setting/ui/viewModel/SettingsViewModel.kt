package com.example.playlistmaker.setting.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.setting.domain.SettingsInteractor
import com.example.playlistmaker.setting.domain.ThemeSettings
import com.example.playlistmaker.sharing.domain.EmailData
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val switchAppThemeInteractor: SettingsInteractor
) : ViewModel() {

    private val darkThemeState = MutableLiveData<ThemeSettings>()

    init {
        loadTheme()
    }

    private fun loadTheme() {
        darkThemeState.value = getCurrentDarkThemeState()
    }

    fun getDarkThemeState(): LiveData<ThemeSettings> = darkThemeState

    fun setCurrentDarkThemeState(darkThemeState: ThemeSettings) {
        switchAppThemeInteractor.updateThemeSetting(darkThemeState)
        this.darkThemeState.value = darkThemeState
    }

    fun getCurrentDarkThemeState(): ThemeSettings {
        return switchAppThemeInteractor.getThemeSettings()
    }

    fun shareApp(shareLink: String) {
        sharingInteractor.shareApp(shareLink)
    }

    fun openSupport(emailData: EmailData) {
        sharingInteractor.openSupport(emailData)
    }

    fun userAgreement(openLink: String) {
        sharingInteractor.userAgreement(openLink)
    }
}