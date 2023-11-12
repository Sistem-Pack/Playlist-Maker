package com.practicum.playlistmaker.ui.settings.view_model

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.model.ThemeSettings
import com.practicum.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    fun sendMail() {

    }

    fun shareApp() {
        Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(ContextCompat.getString(R.string.practicum_offer))
            ContextCompat.startActivity()
        }
        sharingInteractor.shareApp()
    }

    fun openOffer(offerLink: String) {

    }

    companion object {
        fun getViewModelFactory(trackId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(
                        Creator.provideSharingInteractor(),
                        Creator.provideSettingsInteractor()
                    ) as T
                }
            }
    }

}