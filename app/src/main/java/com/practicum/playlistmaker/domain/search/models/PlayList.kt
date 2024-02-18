package com.practicum.playlistmaker.domain.search.models

data class PlayList(
    val playListId: Int,
    val name: String,
    val description: String,
    val image: String?,
    val tracksCount: Int
)