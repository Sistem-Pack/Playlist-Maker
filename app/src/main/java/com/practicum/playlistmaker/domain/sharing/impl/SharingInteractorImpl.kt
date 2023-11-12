package com.practicum.playlistmaker.domain.sharing.impl

import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.sharing.ExternalNavigator
import com.practicum.playlistmaker.domain.sharing.SharingInteractor

class SharingInteractorImpl(
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
        Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(getString(R.string.practicum_offer))
            startActivity(this)
        }
    }

    private fun getSupportEmailData(): EmailData {
        // Нужно реализовать
    }

    private fun getTermsLink(): String {
        // Нужно реализовать
    }
}