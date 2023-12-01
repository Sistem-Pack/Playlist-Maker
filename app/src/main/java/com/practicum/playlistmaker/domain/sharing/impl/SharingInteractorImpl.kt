package com.practicum.playlistmaker.domain.sharing.impl

import android.app.Application
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.sharing.ExternalNavigator
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.model.EmailData

class SharingInteractorImpl(
    val app: Application,
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return app.getString(R.string.share_link)
    }

    private fun getSupportEmailData(): EmailData {
        val messageTo = app.getString(R.string.user_email)
        val messageSubject = app.getString(R.string.support_subject)
        val messageText = app.getString(R.string.support_message)
        return EmailData(messageTo, messageSubject, messageText)
    }

    private fun getTermsLink(): String {
        return app.getString(R.string.practicum_offer)
    }
}