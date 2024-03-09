package com.practicum.playlistmaker.domain.search.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayList(
    val playListId: Int,
    val name: String,
    val description: String,
    val image: String?,
    val tracksCount: Int
) : Parcelable