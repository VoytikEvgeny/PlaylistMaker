package com.example.playlistmaker.sharing.domain.impl

import android.content.Intent
import com.example.playlistmaker.sharing.domain.AgreementData
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.ShareData
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.SharingRepository
import com.example.playlistmaker.sharing.domain.SupportData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val repository: SharingRepository,
) : SharingInteractor {
    override fun shareApp(): Intent {
        return externalNavigator.navigateToShare(getShareData())
    }

    override fun openTerms(): Intent {
        return externalNavigator.navigateToAgreement(getAgreementData())
    }

    override fun openSupport(): Intent {
        return externalNavigator.navigateToSupport(getSupportData())
    }

    override fun getShareError(): String {
        return repository.getShareError()
    }

    override fun getUserAgreementError(): String {
        return repository.getUserAgreementError()
    }

    override fun getSupportError(): String {
        return repository.getSupportError()
    }

    private fun getShareData(): ShareData {
        return ShareData(repository.getShareMessage())
    }

    private fun getSupportData(): SupportData {
        return SupportData(
            repository.getSupportEmail(),
            repository.getSupportSubject(),
            repository.getSupportMessage()
        )
    }

    private fun getAgreementData(): AgreementData {
        return AgreementData(repository.getUserAgreementUrl())
    }
}