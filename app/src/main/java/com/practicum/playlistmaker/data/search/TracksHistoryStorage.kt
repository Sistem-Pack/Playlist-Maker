package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.domain.search.models.Track


interface TracksHistoryStorage {
    fun read(): Array<Track>
    fun saveHistory(tracks: List<Track>)
    fun clear()
}