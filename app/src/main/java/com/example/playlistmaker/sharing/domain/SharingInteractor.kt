package com.example.playlistmaker.sharing.domain

interface SharingInteractor {
    fun shareApp(shareLink: String)
    fun userAgreement(termsLink: String)
    fun openSupport(email: Array<String>, emailSubject: String, emailText: String)
}