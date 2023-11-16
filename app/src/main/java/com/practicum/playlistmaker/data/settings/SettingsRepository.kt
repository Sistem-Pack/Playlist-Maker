package com.practicum.playlistmaker.data.settings.impl

import android.app.Application
import com.practicum.playlistmaker.domain.settings.models.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
    fun setTheme(application: Application, useDarkTheme: ThemeSettings)
}