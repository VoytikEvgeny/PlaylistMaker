package com.example.playlistmaker.setting.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.setting.domain.SettingsInteractor
import com.example.playlistmaker.setting.domain.ThemeSettings
import com.example.playlistmaker.setting.ui.NavigationEvent
import com.example.playlistmaker.setting.ui.SettingsEvent
import com.example.playlistmaker.sharing.data.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {
    private val navigationEvents = MutableLiveData<SettingsEvent>()
    fun getNavigationEvents(): LiveData<SettingsEvent> = navigationEvents

    init {
        loadTheme()
    }

    private fun loadTheme() {
        navigationEvents.value = SettingsEvent.Theme(settingsInteractor.getThemeSettings().isDark)
    }

    fun updateTheme(isDark: Boolean) {
        settingsInteractor.updateThemeSetting(ThemeSettings(isDark))
        navigationEvents.postValue(
            SettingsEvent.Theme(isDark)
        )
    }

    fun getIntent(event: NavigationEvent) {
        when (event) {
            NavigationEvent.SHARE -> shareApp()
            NavigationEvent.SUPPORT -> contactSupport()
            NavigationEvent.AGREEMENT -> openAgreement()
        }
    }

    private fun shareApp() {
        navigationEvents.postValue(
            SettingsEvent.Event(
                intent = sharingInteractor.shareApp(),
                errorMessage = sharingInteractor.getShareError()
            )
        )
    }

    private fun contactSupport() {
        navigationEvents.postValue(
            SettingsEvent.Event(
                intent = sharingInteractor.openSupport(),
                errorMessage = sharingInteractor.getSupportError()
            )
        )
    }

    private fun openAgreement() {
        navigationEvents.postValue(
            SettingsEvent.Event(
                intent = sharingInteractor.openTerms(),
                errorMessage = sharingInteractor.getUserAgreementError()
            )
        )
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val settingsInteractor = Creator.provideSettingsInteractor()
                val sharingInteractor = Creator.provideSharingInteractor()
                SettingsViewModel(sharingInteractor, settingsInteractor)
            }
        }
    }
}