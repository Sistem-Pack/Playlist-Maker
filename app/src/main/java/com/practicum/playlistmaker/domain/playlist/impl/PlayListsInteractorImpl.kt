package com.practicum.playlistmaker.domain.playlist.impl

import android.net.Uri
import com.practicum.playlistmaker.data.playlist.PlayListsRepository
import com.practicum.playlistmaker.domain.playlist.PlayListsInteractor
import com.practicum.playlistmaker.domain.search.models.PlayList
import com.practicum.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class PlayListsInteractorImpl(private val playListsRepository: PlayListsRepository) :
    PlayListsInteractor {

    override suspend fun addPlayList(
        playListName: String,
        playListDescription: String,
        pickImageUri: Uri?
    ) =
        playListsRepository.addPlayList(playListName, playListDescription, pickImageUri)

    override suspend fun editPlayList(
        playListId: Int,
        playListName: String,
        playListDescription: String,
        pickImageUri: Uri?
    ) {
        playListsRepository.editPlayList(
            playListId,
            playListName,
            playListDescription,
            pickImageUri
        )
    }

    override suspend fun getPlayList(playListId: Int): PlayList =
        playListsRepository.getPlayList(playListId)

    override suspend fun deleteTrackFromPlaylist(trackId: Int, playListId: Int) =
        playListsRepository.deleteTrackFromPlaylist(trackId, playListId)

    override suspend fun deletePlaylist(playList: PlayList) =
        playListsRepository.deletePlaylist(playList)

    override fun getTrackCountForPlaylist(playListId: Int): Flow<Int> {
        return playListsRepository.getTrackCountForPlaylist(playListId)
    }

    override suspend fun addTrackToPlayList(track: Track, playListId: Int) =
        playListsRepository.addTrackToPlayList(track, playListId)

    override suspend fun getPlayLists(): ArrayList<PlayList> =
        playListsRepository.getPlayLists()

    override suspend fun getPlayListTracks(playListId: Int): ArrayList<Track> =
        playListsRepository.getPlayListTracks(playListId)

    override suspend fun isTrackInPlayList(trackId: Int, playListId: Int): Boolean =
        playListsRepository.isTrackInPlayList(trackId, playListId)
}