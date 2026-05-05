package com.example.playlistmaker.sharing.data.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.SharingRepository

class SharingRepositoryImpl(private val context: Context) : SharingRepository
{
    override fun getShareMessage(): String = context.getString(R.string.android_course_url)
    override fun getSupportEmail(): String = context.getString(R.string.my_email)
    override fun getSupportSubject(): String = context.getString(R.string.email_subject)
    override fun getSupportMessage(): String = context.getString(R.string.email_text)
    override fun getUserAgreementUrl(): String = context.getString(R.string.practicum_offer)
    override fun getShareError(): String = context.getString(R.string.share_toast)
    override fun getUserAgreementError(): String = context.getString(R.string.agreement_toast)
    override fun getSupportError(): String = context.getString(R.string.support_toast)

}