package com.practicum.playlistmaker.data.settings.impl

import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.creator.Consts
import com.practicum.playlistmaker.domain.settings.SettingsRepository
import com.practicum.playlistmaker.domain.settings.models.ThemeSettings

class SettingsRepositoryImpl: SettingsRepository {

    private val darkTheme = App.sharedPreferences.getBoolean(Consts.SW_MODE, false)

    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(darkTheme)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        App.sharedPreferences.edit()
            .putBoolean(Consts.SW_MODE, settings.darkTheme)
            .apply()
        setTheme(settings)
    }

    override fun setTheme(useDarkTheme: ThemeSettings) {
        AppCompatDelegate.setDefaultNightMode(
            if (useDarkTheme.darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}
