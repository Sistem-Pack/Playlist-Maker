package com.practicum.playlistmaker.data.settings.impl

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.practicum.playlistmaker.Consts
import com.practicum.playlistmaker.domain.settings.SettingsRepository
import com.practicum.playlistmaker.domain.settings.models.ThemeSettings

class SettingsRepositoryImpl(private var sharedPreferences: SharedPreferences): SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(sharedPreferences.getBoolean(Consts.SW_MODE, false))
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPreferences.edit {
            putBoolean(Consts.SW_MODE, settings.darkTheme)
        }
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
