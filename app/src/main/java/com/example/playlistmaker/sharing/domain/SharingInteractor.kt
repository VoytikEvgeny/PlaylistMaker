package com.example.playlistmaker.sharing.domain

interface SharingInteractor {
    fun shareApp(shareLink: String)
    fun userAgreement(openLink: String)
    fun openSupport(emailData: EmailData)
}