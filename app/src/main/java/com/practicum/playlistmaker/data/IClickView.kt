package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.domain.models.Track

interface IClickView {
    fun onClick(track: Track)
}