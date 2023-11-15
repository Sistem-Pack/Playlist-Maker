package com.practicum.playlistmaker.ui.settings.view_model

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.models.App
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
    private val application: App
) : ViewModel() {

    fun sendMail() {
        sharingInteractor.openSupport()
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openOffer() {
        sharingInteractor.openTerms()
    }


}