package com.practicum.playlistmaker.data.playlist

import com.practicum.playlistmaker.domain.search.models.PlayList
import com.practicum.playlistmaker.domain.search.models.Track

interface PlayListsRepository {
    suspend fun addPlayList(playList: PlayList)

    suspend fun addTrackToPlayList(track: Track, playListId: Int)

    suspend fun getPlayLists(): List<PlayList>

    suspend fun getPlayListTracks(playListId: Int): List<Track>

    suspend fun isTrackInPlayList(trackId: Int, playListId: Int): Boolean
}