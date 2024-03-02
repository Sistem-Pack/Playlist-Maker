package com.practicum.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "play_lists_track_table")
class TrackPlayListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val playListId: Int,
    val trackId: Int
)