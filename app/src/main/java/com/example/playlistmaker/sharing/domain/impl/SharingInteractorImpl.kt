package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.models.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
) : SharingInteractor {
    override fun shareApp(shareLink: String) {
        externalNavigator.shareLink(shareLink)
    }

    override fun userAgreement(termsLink: String) {
        externalNavigator.openLink(termsLink)
    }

    override fun openSupport(email: Array<String>, emailSubject: String, emailText: String) {
        externalNavigator.openEmail(getSupportEmailData(email, emailSubject, emailText))
    }

    private fun getSupportEmailData(
        email: Array<String>,
        emailSubject: String,
        emailText: String
    ): EmailData {

        return EmailData(
            email,
            emailSubject,
            emailText
        )

    }
}