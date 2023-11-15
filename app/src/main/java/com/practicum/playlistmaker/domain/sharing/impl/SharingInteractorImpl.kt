package com.practicum.playlistmaker.domain.sharing.impl

import com.practicum.playlistmaker.data.sharing.ExternalNavigator
import com.practicum.playlistmaker.data.contentprovider.ContentProvider
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val contentProvider: ContentProvider
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
        return contentProvider.getStringFromResources("share_link")
    }

    private fun getSupportEmailData(): EmailData {
        val messageTo = contentProvider.getStringFromResources("user_email")
        val messageSubject = contentProvider.getStringFromResources("support_subject")
        val messageText = contentProvider.getStringFromResources("support_message")
        return EmailData(messageTo, messageSubject, messageText)
    }

    private fun getTermsLink(): String {
        return contentProvider.getStringFromResources("practicum_offer")
    }
}