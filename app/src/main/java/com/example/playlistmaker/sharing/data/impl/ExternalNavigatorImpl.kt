package com.example.playlistmaker.sharing.data.impl

import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.sharing.domain.AgreementData
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.ShareData
import com.example.playlistmaker.sharing.domain.SupportData

class ExternalNavigatorImpl(
) : ExternalNavigator {
    override fun navigateToShare(shareData: ShareData): Intent {
        return Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareData.message)
        }
    }

    override fun navigateToSupport(supportData: SupportData): Intent {
        return Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(supportData.email))
            putExtra(Intent.EXTRA_SUBJECT, supportData.subject)
            putExtra(Intent.EXTRA_TEXT, supportData.message)
        }
    }

    override fun navigateToAgreement(agreementData: AgreementData): Intent {
        val url = Uri.parse(agreementData.url)
        return Intent(Intent.ACTION_VIEW, url)
    }
}