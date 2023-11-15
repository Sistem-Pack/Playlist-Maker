package com.practicum.playlistmaker.domain.sharing.model

data class EmailData(
    val mail: String,
    val subject: String,
    val text: String,
    val mailTo:String = "mailto:"
)
