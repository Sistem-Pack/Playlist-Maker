package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.creator.Consts.SEARCH_HISTORY
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.models.Track

class App : Application() {

    //var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences
    private val settingsRepository = Creator.provideSettingsRepository(context = this)
    override fun onCreate() {
        super.onCreate()
        settingsRepository.setTheme(
            application = this,
            useDarkTheme = Creator.provideSettingsInteractor(this).getThemeSettings()
        )
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