package com.practicum.playlistmaker.domain.playlist

import com.practicum.playlistmaker.domain.search.models.PlayList
import com.practicum.playlistmaker.domain.search.models.Track

interface PlayListsInteractor {

    suspend fun addPlayList(playList: PlayList)

    suspend fun addTrackToPlayList(track: Track, playListId: Int)

    suspend fun getPlayLists(): List<PlayList>

    suspend fun getPlayListTracks(playListId: Int): List<Track>

    suspend fun isTrackInPlayList(trackId: Int, playListId: Int): Boolean

}