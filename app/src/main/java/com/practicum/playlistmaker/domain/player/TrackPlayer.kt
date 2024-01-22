package com.practicum.playlistmaker.domain.player

import com.practicum.playlistmaker.domain.player.models.PlayerState

interface TrackPlayer {
    var playerState: PlayerState
    fun preparePlayer(url: String)
    fun startPlayer(url: String)
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPosition(): Int
    fun setOnCompletionListener(listener: (PlayerState) -> Unit)
}