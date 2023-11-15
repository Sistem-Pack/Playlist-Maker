package com.practicum.playlistmaker.data.settings.impl

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker.creator.Consts
import com.practicum.playlistmaker.domain.settings.models.ThemeSettings

class SettingsRepositoryImpl(context: Context) : SettingsRepository {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Consts.USER_PREFS, Application.MODE_PRIVATE)
    private val darkTheme = sharedPreferences.getBoolean(Consts.SW_MODE, false)

    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(darkTheme)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPreferences.edit()
            .putBoolean(Consts.SW_MODE, settings.darkTheme)
            .apply()
    }

}
