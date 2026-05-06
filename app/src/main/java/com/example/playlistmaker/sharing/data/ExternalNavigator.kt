package com.example.playlistmaker.sharing.data

import android.content.Intent
import com.example.playlistmaker.sharing.domain.AgreementData
import com.example.playlistmaker.sharing.domain.ShareData
import com.example.playlistmaker.sharing.domain.SupportData

interface ExternalNavigator {
    fun navigateToShare(shareData: ShareData): Intent
    fun navigateToSupport(supportData: SupportData): Intent
    fun navigateToAgreement(agreementData: AgreementData): Intent
}