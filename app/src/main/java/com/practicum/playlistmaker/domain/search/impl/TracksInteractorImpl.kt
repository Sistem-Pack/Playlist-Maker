package com.practicum.playlistmaker.domain.search.impl

import com.practicum.playlistmaker.data.network.IDataLoadCallback
import com.practicum.playlistmaker.domain.search.api.ITrackInteractor
import com.practicum.playlistmaker.domain.search.api.ITracksRepository
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: ITracksRepository) : ITrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun search(expression: String, callback: IDataLoadCallback) {
        executor.execute {
            val tracks = repository.search(expression)
            val code = repository.errorCode
            callback.onDataLoaded(tracks)
            callback.onError(code)
        }
    }
}