package com.example.playlistmaker.sharing.domain

interface ExternalNavigator {
    fun shareLink(link: String)
    fun openEmail(emailData: EmailData)
    fun openLink(link: String)
}