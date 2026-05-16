package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.domain.EmailData
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
) : SharingInteractor {
    override fun shareApp(shareLink: String) {
        externalNavigator.shareLink(shareLink)
    }

    override fun userAgreement(openLink: String) {
        externalNavigator.openLink(openLink)
    }

    override fun openSupport(emailData: EmailData) {
        externalNavigator.openEmail(emailData)
    }

}