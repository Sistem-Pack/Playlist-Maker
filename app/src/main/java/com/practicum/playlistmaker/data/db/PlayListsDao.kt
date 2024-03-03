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
    suspend fun addPlayList(playList: PlayListEntity)

    @Update(entity = PlayListEntity::class)
    suspend fun editPlayList(playList: PlayListEntity)

    @Insert(
        entity = PlayListsTrackEntity::class,
        onConflict = OnConflictStrategy.IGNORE
    ) // добавляет трек
    suspend fun addPlayListsTrack(playListsTrackEntity: PlayListsTrackEntity)

    @Insert(entity = TrackPlayListEntity::class)
    suspend fun addTrackPlayList(trackPlayListEntity: TrackPlayListEntity)

    @Transaction
    suspend fun addTrackToPlayList(
        playListsTrackEntity: PlayListsTrackEntity,
        trackPlayListEntity: TrackPlayListEntity
    ) {
        addPlayListsTrack(playListsTrackEntity)
        addTrackPlayList(trackPlayListEntity)
    }

    @Query("SELECT playListId, name, description, image, (SELECT COUNT(id) FROM play_lists_track_table WHERE play_lists_track_table.playListId=play_lists_table.playListId) as tracksCount FROM play_lists_table WHERE playListId = :playListId")
    suspend fun getPlayList(playListId: Int): PlayListWithCountTracks

    @Query("SELECT playListId, name, description, image, (SELECT COUNT(id) FROM play_lists_track_table WHERE play_lists_track_table.playListId=play_lists_table.playListId) as tracksCount FROM play_lists_table ORDER BY playListId DESC")
    suspend fun getPlayLists(): List<PlayListWithCountTracks>

    @Query("SELECT track_play_lists_table.* FROM track_play_lists_table LEFT JOIN play_lists_track_table ON track_play_lists_table.trackId=play_lists_track_table.trackId WHERE play_lists_track_table.playListId = :playListId  ORDER BY play_lists_track_table.id DESC")
    suspend fun getPlayListTracks(playListId: Int): List<PlayListsTrackEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM play_lists_track_table  WHERE trackId = :trackId AND playListId = :playListId)")
    suspend fun isTrackInPlayList(trackId: Int, playListId: Int): Boolean

    @Query("DELETE FROM track_play_lists_table WHERE trackId NOT IN (SELECT DISTINCT(trackId) FROM play_lists_track_table)")
    suspend fun clearTracks()

    @Query("DELETE FROM play_lists_track_table WHERE playListId = :playListId AND trackId = :trackId")
    suspend fun deleteTrackFromTrackPlayList(playListId: Int, trackId: Int)

    @Transaction
    suspend fun deleteTrackFromPlayList(
        trackId: Int,
        playListId: Int
    ) {
        deleteTrackFromTrackPlayList(playListId, trackId)
        clearTracks()
    }

    @Query("DELETE FROM play_lists_table WHERE playListId = :playListId")
    suspend fun deletePlayListFromPlayList(playListId: Int)

    @Query("DELETE FROM play_lists_track_table WHERE playListId = :playListId")
    suspend fun deletePlayListFromTrackPlayList(playListId: Int)

    @Transaction
    suspend fun deletePlayList(playListId: Int) {
        deletePlayListFromPlayList(playListId)
        deletePlayListFromTrackPlayList(playListId)
        clearTracks()
    }

    @Query("SELECT COUNT(*) FROM play_lists_track_table WHERE playListId = :playListId")
    fun getTrackCountForPlaylist(playListId: Int): Flow<Int>
}