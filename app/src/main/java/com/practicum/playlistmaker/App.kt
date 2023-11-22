package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.data.settings.impl.SettingsRepository

class App : Application() {

    private lateinit var settingsRepository : SettingsRepository
    override fun onCreate() {
        super.onCreate()
        settingsRepository = Creator.provideSettingsRepository(context = this)
        settingsRepository.setTheme(
            useDarkTheme = Creator.provideSettingsInteractor(this).getThemeSettings()
        )
    }

}