package com.practicum.playlistmaker.domain.favorite.impl

import com.practicum.playlistmaker.domain.favorite.FavoriteTracksInteractor
import com.practicum.playlistmaker.domain.favorite.FavoriteTracksRepository
import com.practicum.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(
    private val repository: FavoriteTracksRepository
) : FavoriteTracksInteractor {

    override suspend fun insertTrack(track: Track) =
        repository.insertTrack(track)

    override suspend fun deleteTrack(track: Track) =
        repository.deleteTrack(track)

    override suspend fun getTracks(): Flow<List<Track>> =
        repository.getTracks()

    override suspend fun getIdsTracks(): Flow<List<Int>> =
        repository.getIdsTracks()
}