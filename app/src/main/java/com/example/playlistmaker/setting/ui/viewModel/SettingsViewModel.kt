package com.example.playlistmaker.setting.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.setting.domain.ThemeSettings
import com.example.playlistmaker.sharing.domain.EmailData

class SettingsViewModel() : ViewModel() {
    private val SharingInteractor = Creator.provideSharingInteractor()
    private val SwitchAppThemeInteractor = Creator.provideSettingsInteractor()

    private val darkThemeState = MutableLiveData<ThemeSettings>()

    init {
        loadTheme()
    }

    private fun loadTheme() {
        darkThemeState.value = getCurrentDarkThemeState()
    }
    fun getDarkThemeState(): LiveData<ThemeSettings> = darkThemeState

    fun setCurrentDarkThemeState(darkThemeState: ThemeSettings) {
        SwitchAppThemeInteractor.updateThemeSetting(darkThemeState)
        this.darkThemeState.value = darkThemeState
    }

    fun getCurrentDarkThemeState(): ThemeSettings {
        return SwitchAppThemeInteractor.getThemeSettings()
    }

    fun shareApp(shareLink: String) {
        SharingInteractor.shareApp(shareLink)
    }

    fun openSupport(emailData: EmailData) {
        SharingInteractor.openSupport(emailData)
    }

    fun userAgreement(openLink: String) {
        SharingInteractor.userAgreement(openLink)
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel()
            }
        }
    }
}