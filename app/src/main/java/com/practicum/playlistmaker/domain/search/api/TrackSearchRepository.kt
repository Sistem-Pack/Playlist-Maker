package com.practicum.playlistmaker.domain.search.api

import com.practicum.playlistmaker.domain.search.models.Track

interface TrackSearchRepository {
    val errorCode: Int
    fun search(expression: String): List<Track>

    fun readSearchHistory(): ArrayList<Track>

    fun saveHistory(tracks: List<Track>)

    fun clearSearchHistory()

}