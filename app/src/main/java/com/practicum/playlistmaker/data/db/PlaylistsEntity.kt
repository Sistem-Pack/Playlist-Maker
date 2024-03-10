package com.practicum.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true)
    val playListId: Int?,
    val name: String,
    val description: String,
    val image: String?
)

data class PlayListWithCountTracks(
    val playListId: Int?,
    val name: String,
    val description: String,
    val image: String?,
    val tracksCount: Int
)