package com.practicum.playlistmaker.data.playlist

import android.net.Uri
import com.practicum.playlistmaker.domain.search.models.PlayList
import com.practicum.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface PlayListsRepository {
    suspend fun addPlayList(playListName: String, playListDescription: String, pickImageUri: Uri?)
    suspend fun editPlayList(playListId: Int, playListName: String, playListDescription: String, pickImageUri: Uri?)
    suspend fun addTrackToPlayList(track: Track, playListId: Int)
    suspend fun getPlayList(playListId: Int): PlayList
    suspend fun getPlayLists(): List<PlayList>
    suspend fun getPlayListTracks(playListId: Int): List<Track>
    suspend fun isTrackInPlayList(trackId: Int, playListId: Int): Boolean
    suspend fun deleteTrackFromPlaylist(trackId: Int, playListId: Int)
    suspend fun deletePlaylist(playList: PlayList)
    fun getTrackCountForPlaylist(playListId: Int): Flow<Int>
}