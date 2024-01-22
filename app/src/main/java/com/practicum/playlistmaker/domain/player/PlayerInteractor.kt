package com.practicum.playlistmaker.domain.player

import com.practicum.playlistmaker.domain.player.models.PlayerState

interface PlayerInteractor {
    fun preparePlayer(url: String)
    fun startPlayer(url: String)
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPosition(): Int
    fun getPlayerState(): PlayerState
    fun setTrackCompletionListener(listener: (PlayerState) -> Unit)
}