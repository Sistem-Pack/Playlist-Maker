package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import com.practicum.playlistmaker.creator.Consts
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.settings.SettingsRepository

class App : Application() {

    companion object {
        lateinit var sharedPreferences: SharedPreferences
    }

    private lateinit var settingsRepository : SettingsRepository

    override fun onCreate() {
        super.onCreate()
        Creator.initApp(this)
        sharedPreferences = getSharedPreferences(Consts.USER_PREFS, MODE_PRIVATE)
        settingsRepository = Creator.provideSettingsRepository()
        settingsRepository.setTheme(
            useDarkTheme = Creator.provideSettingsInteractor().getThemeSettings()
        )
    }

}