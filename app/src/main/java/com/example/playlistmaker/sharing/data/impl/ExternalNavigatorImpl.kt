package com.example.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.example.playlistmaker.sharing.domain.EmailData
import com.example.playlistmaker.sharing.domain.ExternalNavigator

class ExternalNavigatorImpl(val context: Context
) : ExternalNavigator {
    override fun shareLink(link: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, link)

        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(shareIntent, null)
    }

    override fun openEmail(emailData: EmailData) {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data = "mailto:".toUri()
        supportIntent.putExtra(Intent.EXTRA_EMAIL, emailData.email)
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
        supportIntent.putExtra(Intent.EXTRA_TEXT, emailData.text)

        supportIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity( supportIntent, null)
    }

    override fun openLink(link: String) {
        val userIntent = Intent(Intent.ACTION_VIEW)
        userIntent.data = link.toUri()

        userIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity( userIntent, null)
    }
}