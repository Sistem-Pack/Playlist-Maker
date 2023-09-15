package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    private companion object {
        const val USER_PREFS = "PREFS"
        const val SW_MODE = "SWITCH_MODE"
    }

    var darkTheme = false
    private lateinit var sharedPrefs : SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(SW_MODE, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        //sharedPrefs.edit(SW_MODE, darkTheme)
    }
}