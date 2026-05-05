package com.example.playlistmaker.sharing.domain

import android.content.Intent

interface ExternalNavigator {
    fun navigateToShare(shareData: ShareData): Intent
    fun navigateToSupport(supportData: SupportData): Intent
    fun navigateToAgreement(agreementData: AgreementData): Intent
}