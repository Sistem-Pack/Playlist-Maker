package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface ITracksRepository {
    abstract val errorCode: Int
    fun search(expression: String): List<Track>
}