package com.practicum.playlistmaker.data.playlist.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.practicum.playlistmaker.Consts
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.PlayListEntity
import com.practicum.playlistmaker.data.db.PlayListWithCountTracks
import com.practicum.playlistmaker.data.db.PlayListsTrackEntity
import com.practicum.playlistmaker.data.db.PlaylistsTrackDbConverter
import com.practicum.playlistmaker.data.db.TrackPlayListEntity
import com.practicum.playlistmaker.data.playlist.PlayListsRepository
import com.practicum.playlistmaker.domain.search.models.PlayList
import com.practicum.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class PlayListsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playListsTrackDbConverter: PlaylistsTrackDbConverter,
    private val context: Context
) : PlayListsRepository {

    private val albumImagesFilePath = File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        Consts.PLAY_LISTS_IMAGES_DIRECTORY
    )

    override suspend fun addPlayList(
        playListName: String,
        playListDescription: String,
        pickImageUri: Uri?
    ) {
        var imageFileName: String? = null
        if (pickImageUri != null) {
            imageFileName = saveAlbumImage(pickImageUri)
        }
        appDatabase.playListsTrackDao().addPlayList(
            PlayListEntity(
                null,
                playListName,
                playListDescription,
                imageFileName
            )
        )
    }

    override suspend fun editPlayList(
        playListId: Int,
        playListName: String,
        playListDescription: String,
        pickImageUri: Uri?
    ) {
        val playList = appDatabase.playListsTrackDao().getPlayList(playListId)

        var imageFileName = playList.image
        if (pickImageUri != null) {
            if (playList.image != null) {
                deleteAlbumImage(playList.image)
            }
            imageFileName = saveAlbumImage(pickImageUri)
        }

        appDatabase.playListsTrackDao().editPlayList(
            PlayListEntity(
                playListId,
                playListName,
                playListDescription,
                imageFileName
            )
        )
    }

    override suspend fun addTrackToPlayList(track: Track, playListId: Int) =
        appDatabase.playListsTrackDao().addTrackToPlayList(
            playListsTrackEntity = playListsTrackDbConverter.map(track),
            trackPlayListEntity = TrackPlayListEntity(null, playListId, track.trackId)
        )

    override suspend fun getPlayList(playListId: Int): PlayList =
        playListsTrackDbConverter.map(
            appDatabase.playListsTrackDao().getPlayList(playListId)
        )

    override suspend fun getPlayLists(): List<PlayList> = convertPlayListWithCountTracksToPlayList(
        appDatabase.playListsTrackDao().getPlayLists()
    )

    override suspend fun getPlayListTracks(playListId: Int): List<Track> =
        convertPlayListsTrackEntityToTrack(
            appDatabase.playListsTrackDao().getPlayListTracks(playListId)
        )

    override suspend fun deleteTrackFromPlaylist(trackId: Int, playListId: Int) {
        appDatabase.playListsTrackDao().deleteTrackFromPlayList(trackId, playListId)
    }

    override suspend fun deletePlaylist(playList: PlayList) {
        playList.image?.let {
            deleteAlbumImage(it)
        }
        appDatabase.playListsTrackDao().deletePlayList(playList.playListId)
    }

    private fun deleteAlbumImage(imageFileName: String) {
        if (File(albumImagesFilePath, imageFileName).exists()) {
            File(albumImagesFilePath, imageFileName).delete()
        }
    }

    private fun saveAlbumImage(uri: Uri): String {
        val imageFileName = Calendar.getInstance().timeInMillis.toString() + ".jpg"
        if (!albumImagesFilePath.exists()) {
            albumImagesFilePath.mkdirs()
        }
        BitmapFactory
            .decodeStream(
                context.contentResolver.openInputStream(uri)
            )
            .compress(
                Bitmap.CompressFormat.JPEG,
                Consts.IMAGE_QUALITY,
                FileOutputStream(
                    File(albumImagesFilePath, imageFileName)
                )
            )
        return imageFileName
    }

    override suspend fun isTrackInPlayList(trackId: Int, playListId: Int): Boolean =
        appDatabase.playListsTrackDao().isTrackInPlayList(trackId, playListId)

    private fun convertPlayListsTrackEntityToTrack(tracks: List<PlayListsTrackEntity>): List<Track> =
        tracks.map { playListsTrackDbConverter.map(it) }

    private fun convertPlayListWithCountTracksToPlayList(playListWithCountTracks: List<PlayListWithCountTracks>): List<PlayList> =
        playListWithCountTracks.map {
            playListsTrackDbConverter.map(it)
        }

    override fun getTrackCountForPlaylist(playListId: Int): Flow<Int> {
        return appDatabase.playListsTrackDao().getTrackCountForPlaylist(playListId)
    }

}