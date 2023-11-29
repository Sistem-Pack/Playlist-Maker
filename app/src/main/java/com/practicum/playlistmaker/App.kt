package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.di.viewModelModel
import com.practicum.playlistmaker.domain.settings.SettingsRepository
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    private lateinit var themeSwitch: SettingsRepository

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, interactorModule, repositoryModule, viewModelModel)
        }
        themeSwitch = getKoin().get()
        themeSwitch.setTheme(themeSwitch.getThemeSettings())
    }


}