package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.domain.search.models.Track

interface IDataLoadCallback {
    fun onDataLoaded(tracksL: List<Track>)
    fun onError(code: Int)
}