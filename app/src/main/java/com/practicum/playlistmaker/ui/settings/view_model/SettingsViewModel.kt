package com.practicum.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.models.ThemeSettings
import com.practicum.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    private val _themeSettingsState = MutableLiveData<ThemeSettings>()
    val themeSettingsState: LiveData<ThemeSettings> = _themeSettingsState

    init {
        _themeSettingsState.value = settingsInteractor.getThemeSettings()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        _themeSettingsState.value = ThemeSettings(darkThemeEnabled)
        settingsInteractor.updateThemeSetting(ThemeSettings(darkThemeEnabled))
    }

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