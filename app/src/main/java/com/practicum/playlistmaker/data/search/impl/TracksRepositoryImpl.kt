package com.practicum.playlistmaker.data.search.impl

import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.data.search.TracksHistoryStorage
import com.practicum.playlistmaker.data.search.dto.TrackSearchRequest
import com.practicum.playlistmaker.data.search.dto.TrackSearchResponse
import com.practicum.playlistmaker.domain.search.api.TrackSearchRepository
import com.practicum.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(private val networkClient: NetworkClient, private val tracksHistoryStorage: TracksHistoryStorage) : TrackSearchRepository {
    override var errorCode: Int = 0

    override fun search(expression: String): Flow<ArrayList<Track>> = flow {//ArrayList<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        errorCode = response.resultCode
        if (errorCode == 200) {
            emit((response as TrackSearchResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            } as ArrayList<Track>)
        } else {
            emit(arrayListOf())
        }
    }

    override fun readSearchHistory(): ArrayList<Track> {
        return tracksHistoryStorage.read().toCollection(ArrayList())
    }

    override fun saveHistory(tracks: List<Track>) {
        tracksHistoryStorage.saveHistory(tracks)
    }

    override fun clearSearchHistory() {
        tracksHistoryStorage.clear()
    }

}
