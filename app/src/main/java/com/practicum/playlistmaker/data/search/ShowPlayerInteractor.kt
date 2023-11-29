package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.domain.search.models.Track

interface ShowPlayerInteractor {
    fun openPlayer(track: Track)

}