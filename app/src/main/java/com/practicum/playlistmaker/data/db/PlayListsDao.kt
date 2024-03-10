package com.practicum.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayListsDao {
    @Insert(entity = PlayListEntity::class)
    fun addPlayList(playList: PlayListEntity)

    @Update(entity = PlayListEntity::class)
    fun editPlayList(playList: PlayListEntity)

    @Insert(
        entity = PlayListsTrackEntity::class,
        onConflict = OnConflictStrategy.IGNORE
    ) // добавляет трек
    fun addPlayListsTrack(playListsTrackEntity: PlayListsTrackEntity)

    @Insert(entity = TrackPlayListEntity::class)
    fun addTrackPlayList(trackPlayListEntity: TrackPlayListEntity)

    @Transaction
    fun addTrackToPlayList(
        playListsTrackEntity: PlayListsTrackEntity,
        trackPlayListEntity: TrackPlayListEntity
    ) {
        addPlayListsTrack(playListsTrackEntity)
        addTrackPlayList(trackPlayListEntity)
    }

    @Query("SELECT playListId, name, description, image, (SELECT COUNT(id) FROM playlists_track_table WHERE playlists_track_table.playListId=playlists_table.playListId) as tracksCount FROM playlists_table WHERE playListId = :playListId")
    fun getPlayList(playListId: Int): PlayListWithCountTracks

    @Query("SELECT playListId, name, description, image, (SELECT COUNT(id) FROM playlists_track_table WHERE playlists_track_table.playListId=playlists_table.playListId) as tracksCount FROM playlists_table ORDER BY playListId DESC")
    fun getPlayLists(): List<PlayListWithCountTracks>

    @Query("SELECT track_playlists_table.* FROM track_playlists_table LEFT JOIN playlists_track_table ON track_playlists_table.trackId=playlists_track_table.trackId WHERE playlists_track_table.playListId = :playListId  ORDER BY playlists_track_table.id DESC")
    fun getPlayListTracks(playListId: Int): List<PlayListsTrackEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM playlists_track_table  WHERE trackId = :trackId AND playListId = :playListId)")
    fun isTrackInPlayList(trackId: Int, playListId: Int): Boolean

    @Query("DELETE FROM track_playlists_table WHERE trackId NOT IN (SELECT DISTINCT(trackId) FROM playlists_track_table)")
    fun clearTracks()

    @Query("DELETE FROM playlists_track_table WHERE playListId = :playListId AND trackId = :trackId")
    fun deleteTrackFromTrackPlayList(playListId: Int, trackId: Int)

    @Transaction
    fun deleteTrackFromPlayList(
        trackId: Int,
        playListId: Int
    ) {
        deleteTrackFromTrackPlayList(playListId, trackId)
        clearTracks()
    }

    @Query("DELETE FROM playlists_table WHERE playListId = :playListId")
    fun deletePlayListFromPlayList(playListId: Int)

    @Query("DELETE FROM playlists_track_table WHERE playListId = :playListId")
    fun deletePlayListFromTrackPlayList(playListId: Int)

    @Transaction
    fun deletePlayList(playListId: Int) {
        deletePlayListFromPlayList(playListId)
        deletePlayListFromTrackPlayList(playListId)
        clearTracks()
    }

    @Query("SELECT COUNT(*) FROM playlists_track_table WHERE playListId = :playListId")
    fun getTrackCountForPlaylist(playListId: Int): Flow<Int>
}