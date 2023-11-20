package com.practicum.playlistmaker.domain.search.api

import com.practicum.playlistmaker.data.network.IDataLoadCallback
import com.practicum.playlistmaker.domain.search.models.Track

interface TrackSearchInteractor {
    fun search(expression: String, consumer: TracksConsumer)
    interface TracksConsumer {
        fun consume(foundTracks: List<Track>, errorMessage: String?)
    }

    fun readSearchHistory(): ArrayList<Track>

    fun saveHistory(tracks: List<Track>)

    fun clearSearchHistory()

}