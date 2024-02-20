package com.practicum.playlistmaker.domain.playlist.impl

import com.practicum.playlistmaker.data.playlist.PlayListsRepository
import com.practicum.playlistmaker.domain.playlist.PlayListsInteractor
import com.practicum.playlistmaker.domain.search.models.PlayList
import com.practicum.playlistmaker.domain.search.models.Track

class PlayListsInteractorImpl(private val playListsRepository: PlayListsRepository) :
    PlayListsInteractor {
    override suspend fun addPlayList(playList: PlayList) =
        playListsRepository.addPlayList(playList)

    override suspend fun addTrackToPlayList(track: Track, playListId: Int) =
        playListsRepository.addTrackToPlayList(track, playListId)

    override suspend fun getPlayLists(): List<PlayList> =
        playListsRepository.getPlayLists()

    override suspend fun getPlayListTracks(playListId: Int): List<Track> =
        playListsRepository.getPlayListTracks(playListId)

    override suspend fun isTrackInPlayList(trackId: Int, playListId: Int): Boolean =
        playListsRepository.isTrackInPlayList(trackId, playListId)
}