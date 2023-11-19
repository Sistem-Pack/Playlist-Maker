package com.practicum.playlistmaker.domain.search.api

import com.practicum.playlistmaker.data.network.IDataLoadCallback
import com.practicum.playlistmaker.domain.search.models.Track

interface ITrackInteractor {
    fun search(expression: String, callback: IDataLoadCallback)
    interface TracksConsumer {
        fun consume(foundTracks: List<Track>)
    }

}