package com.practicum.playlistmaker.data.sharing

import com.practicum.playlistmaker.domain.models.Track

interface ExternalNavigator {
    fun shareLink(shareAppLink: String)
    fun openLink(termsLink: String)
    fun openEmail(supportEmailData: String)

}