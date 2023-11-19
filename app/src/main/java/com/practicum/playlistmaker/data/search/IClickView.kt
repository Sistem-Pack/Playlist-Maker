package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.domain.search.models.Track

interface IClickView {
    fun onClick(track: Track)
}