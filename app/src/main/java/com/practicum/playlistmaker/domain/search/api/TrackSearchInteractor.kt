package com.practicum.playlistmaker.domain.search.api

import com.practicum.playlistmaker.data.network.IDataLoadCallback
import com.practicum.playlistmaker.domain.search.models.Track

interface TrackSearchInteractor {
    fun search(expression: String, callback: IDataLoadCallback)
    interface TracksConsumer {
        fun consume(foundTracks: List<Track>)
    }

    fun readSearchHistory(): ArrayList<Track>

    fun saveHistory(tracks: List<Track>)

    fun clearSearchHistory()

}