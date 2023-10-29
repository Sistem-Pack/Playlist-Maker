package com.practicum.playlistmaker.domain.models

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.models.Track

class App : Application() {

    private companion object {
        const val USER_PREFS = "PREFS"
        const val SW_MODE = "SWITCH_MODE"
        const val SEARCH_HISTORY = "HISTORY_TRACKS"
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
        sharedPrefs.edit()
            .putBoolean(SW_MODE, darkTheme)
            .apply()
    }
    fun writeSearchHistory(tracks: ArrayList<Track>) {
        val json = createJsonStringTracks(tracks)
        sharedPrefs.edit()
            .putString(SEARCH_HISTORY, json)
            .apply()
    }

    fun readSearchHistory(): ArrayList<Track> {
        val json = sharedPrefs.getString(SEARCH_HISTORY, null) ?: return ArrayList<Track>()
        return createTracksFromJson(json)
    }

    private fun createJsonStringTracks(tracks: ArrayList<Track>): String {
        return Gson().toJson(tracks)
    }

    private fun createTracksFromJson(json: String): ArrayList<Track> {
        val token = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, token)
    }

}