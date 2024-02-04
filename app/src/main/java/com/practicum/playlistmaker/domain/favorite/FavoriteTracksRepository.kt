package com.practicum.playlistmaker.domain.favorite

import com.practicum.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {
    suspend fun insertTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    suspend fun getTracks(): Flow<List<Track>>
    suspend fun getIdsTracks(): Flow<List<Int>>
}