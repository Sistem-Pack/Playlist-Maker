package com.practicum.playlistmaker.ui.settings.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.creator.Creator

class SettingsViewModelFactory(context: Context): ViewModelProvider.Factory {

    private val settingsInteractor = Creator.provideSettingsInteractor()
    private val sharingInteractor = Creator.provideSharingInteractor(context = context)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            settingsInteractor = settingsInteractor,
            sharingInteractor = sharingInteractor
        ) as T
    }
}