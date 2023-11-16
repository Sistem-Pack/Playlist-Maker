package com.practicum.playlistmaker.data.search.dto

data class TrackSearchResponse(var resultCount: Int, var results: List<TrackDto>) : Response()