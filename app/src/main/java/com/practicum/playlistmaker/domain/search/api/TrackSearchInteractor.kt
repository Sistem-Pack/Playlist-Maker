package com.practicum.playlistmaker.domain.search.api

import com.practicum.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackSearchInteractor {
    /*fun search(expression: String, consumer: TracksConsumer)
    interface TracksConsumer {
        fun consume(foundTracks: ArrayList<Track>?, errorMessage: String?)
    }*/

    fun search(expression : String) : Flow<Pair<ArrayList<Track>?, String?>>

    fun readSearchHistory(): ArrayList<Track>

    fun saveHistory(tracks: List<Track>)

    fun clearSearchHistory()

}