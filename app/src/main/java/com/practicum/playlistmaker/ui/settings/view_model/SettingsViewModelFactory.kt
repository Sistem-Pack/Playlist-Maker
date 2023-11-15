package com.practicum.playlistmaker.ui.settings.view_model

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.App

class SettingsViewModelFactory(context: Context, application: Application): ViewModelProvider.AndroidViewModelFactory(application) {

    private val settingsInteractor = Creator.provideSettingsInteractor(context)
    private val sharingInteractor = Creator.provideSharingInteractor(context)
    private val getApplication = application as App

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            settingsInteractor = settingsInteractor,
            sharingInteractor = sharingInteractor,
            application = getApplication
        ) as T
    }
}