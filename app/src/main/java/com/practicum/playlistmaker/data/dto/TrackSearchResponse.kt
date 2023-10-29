package com.practicum.playlistmaker.data.dto

data class TrackSearchResponse(var resultCount: Int, var results: List<TrackDto>) : Response()