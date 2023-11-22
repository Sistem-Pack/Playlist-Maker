package com.practicum.playlistmaker.data.search.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.creator.Consts
import com.practicum.playlistmaker.data.search.TracksHistoryStorage
import com.practicum.playlistmaker.domain.search.models.Track

class TracksHistoryStorageImpl(private val sharedPreferences: SharedPreferences) :
    TracksHistoryStorage {
    override fun read(): ArrayList<Track> {
        return createTracksFromJson(
            sharedPreferences.getString(Consts.SEARCH_HISTORY, null) ?: return ArrayList<Track>()
        )
    }

    override fun saveHistory(tracks: List<Track>) {
        val tracksJson = Gson().toJson(tracks)
        sharedPreferences.edit().putString(Consts.SEARCH_HISTORY, tracksJson).apply()
    }

    override fun clear() {
        sharedPreferences.edit()
            .clear()
            .apply()
    }

    private fun createTracksFromJson(json: String): ArrayList<Track> {
        val token = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, token)
    }

}