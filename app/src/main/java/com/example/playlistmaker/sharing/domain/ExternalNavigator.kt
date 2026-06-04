package com.example.playlistmaker.sharing.domain

import com.example.playlistmaker.sharing.domain.models.EmailData

interface ExternalNavigator {
    fun shareLink(link: String)
    fun openEmail(emailData: EmailData)
    fun openLink(link: String)
}