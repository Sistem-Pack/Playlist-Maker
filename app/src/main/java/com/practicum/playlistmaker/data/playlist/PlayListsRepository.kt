package com.practicum.playlistmaker.data.playlist

import com.example.playlistmaker.common.models.Track
import com.example.playlistmaker.common.models.PlayList

interface PlayListsRepository {
    suspend fun addPlayList(playList: PlayList)

    suspend fun addTrackToPlayList(track: Track, playListId: Int)

    suspend fun getPlayLists(): List<PlayList>

    suspend fun getPlayListTracks(playListId: Int): List<Track>

    suspend fun isTrackInPlayList(trackId: Int, playListId: Int): Boolean
}