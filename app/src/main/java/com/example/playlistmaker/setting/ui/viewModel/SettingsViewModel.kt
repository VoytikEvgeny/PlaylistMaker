package com.example.playlistmaker.setting.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.setting.domain.SettingsInteractor
import com.example.playlistmaker.setting.domain.ThemeSettings
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    fun isDarkThemeOn(): Boolean {
        return settingsInteractor.getThemeSettings().isDark
    }

    fun doShare(courseUrl: String) {
        sharingInteractor.shareApp(courseUrl)
    }

    fun changeTheme(checked: Boolean) {
        settingsInteractor.updateThemeSetting(
            ThemeSettings(
                checked
            )
        )
    }

    fun doWrightTechSupport(email: Array<String>, emailSubject: String, emailText: String) {
        sharingInteractor.openSupport(
            email,
            emailSubject,
            emailText
        )
    }

    fun showAgreement(termsLink: String) {
        sharingInteractor.userAgreement(termsLink)
    }

}