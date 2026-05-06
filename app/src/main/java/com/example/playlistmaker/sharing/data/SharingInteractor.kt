package com.example.playlistmaker.sharing.data

import android.content.Intent

interface SharingInteractor {
    fun shareApp(): Intent
    fun openTerms(): Intent
    fun openSupport(): Intent
    fun getShareError(): String
    fun getUserAgreementError(): String
    fun getSupportError(): String
}