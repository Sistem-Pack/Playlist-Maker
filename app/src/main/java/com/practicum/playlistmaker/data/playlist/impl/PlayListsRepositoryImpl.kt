package com.practicum.playlistmaker.data.playlist.impl

import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.PlayListWithCountTracks
import com.practicum.playlistmaker.data.db.PlayListsTrackEntity
import com.practicum.playlistmaker.data.db.PlaylistsTrackDbConvertor
import com.practicum.playlistmaker.data.db.TrackPlayListEntity
import com.practicum.playlistmaker.data.playlist.PlayListsRepository
import com.practicum.playlistmaker.domain.search.models.PlayList
import com.practicum.playlistmaker.domain.search.models.Track

class PlayListsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playListsTrackDbConvertor: PlaylistsTrackDbConvertor
) : PlayListsRepository {

    override suspend fun addPlayList(playList: PlayList) =
        appDatabase.playListsTrackDao().addPlayList(
            playListsTrackDbConvertor.map(playList = playList)
        )

    override suspend fun addTrackToPlayList(track: Track, playListId: Int) =
        appDatabase.playListsTrackDao().addTrackToPlayList(
            playListsTrackEntity = playListsTrackDbConvertor.map(track = track),
            trackPlayListEntity = TrackPlayListEntity(null, playListId, track.trackId)
        )

    override suspend fun getPlayLists(): List<PlayList> = convertPlayListWithCountTracksToPlayList(
        appDatabase.playListsTrackDao().getPlayLists()
    )

    override suspend fun getPlayListTracks(playListId: Int): List<Track> =
        convertPlayListsTrackEntityToTrack(
            appDatabase.playListsTrackDao().getPlayListTracks(playListId)
        )

    override suspend fun isTrackInPlayList(trackId: Int, playListId: Int): Boolean =
        appDatabase.playListsTrackDao().isTrackInPlayList(trackId, playListId)


    private fun convertPlayListsTrackEntityToTrack(tracks: List<PlayListsTrackEntity>): List<Track> =
        tracks.map { playListsTrackDbConvertor.map(playListsTrackEntity = it) }

    private fun convertPlayListWithCountTracksToPlayList(playListWithCountTracks: List<PlayListWithCountTracks>): List<PlayList> =
        playListWithCountTracks.map {
            playListsTrackDbConvertor.map(it)
        }

}