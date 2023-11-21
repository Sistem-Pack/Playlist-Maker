package com.practicum.playlistmaker.data.search.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.creator.Consts
import com.practicum.playlistmaker.data.search.TracksHistoryStorage
import com.practicum.playlistmaker.domain.search.models.Track

class TracksHistoryStorageImpl(private val sharedPreferences: SharedPreferences) :
    TracksHistoryStorage {
    override fun read(): Array<Track> {
        return sharedPreferences.getString(Consts.HISTORY_TRACK_KEY, null)?.let {
            Gson().fromJson(it, Array<Track>::class.java)
        } ?: emptyArray()
    }

    override fun saveHistory(tracks: List<Track>) {
        val tracksJson = Gson().toJson(tracks)
        sharedPreferences.edit().putString(Consts.HISTORY_TRACK_KEY, tracksJson).apply()
    }

    override fun clear() {
        sharedPreferences.edit()
            .clear()
            .apply()
    }

}