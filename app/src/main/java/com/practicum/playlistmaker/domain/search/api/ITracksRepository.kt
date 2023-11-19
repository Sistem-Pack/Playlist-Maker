package com.practicum.playlistmaker.domain.search.api

import com.practicum.playlistmaker.domain.search.models.Track

interface ITracksRepository {
    abstract val errorCode: Int
    fun search(expression: String): List<Track>
}