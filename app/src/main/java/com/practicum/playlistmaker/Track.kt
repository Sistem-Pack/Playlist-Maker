package com.practicum.playlistmaker

data class Track(
    val key: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
)