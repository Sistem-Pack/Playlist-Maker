package com.practicum.playlistmaker.data.settings.impl

import com.practicum.playlistmaker.domain.settings.models.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
    fun setTheme(useDarkTheme: ThemeSettings)
}