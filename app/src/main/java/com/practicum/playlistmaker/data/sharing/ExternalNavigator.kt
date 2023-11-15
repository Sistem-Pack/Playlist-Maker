package com.practicum.playlistmaker.data.sharing

import com.practicum.playlistmaker.domain.sharing.model.EmailData

interface ExternalNavigator {
    fun shareLink(shareAppLink: String)
    fun openLink(termsLink: String)
    fun openEmail(supportEmailData: EmailData)

}