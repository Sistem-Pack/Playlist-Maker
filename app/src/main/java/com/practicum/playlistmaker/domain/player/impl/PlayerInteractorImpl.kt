package com.practicum.playlistmaker.domain.player.impl

import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.models.PlayerState
import com.practicum.playlistmaker.domain.player.TrackPlayer

class PlayerInteractorImpl(private val trackPlayer: TrackPlayer) : PlayerInteractor {

    override fun preparePlayer(url: String, onChangeState: (s: PlayerState) -> Unit) {
        trackPlayer.preparePlayer(url, onChangeState)
    }

    override fun startPlayer() {
        trackPlayer.startPlayer()
    }

    override fun pausePlayer() {
        trackPlayer.pausePlayer()
    }

    override fun releasePlayer() {
        trackPlayer.releasePlayer()
    }

    override fun getCurrentPosition(): Int {
        return trackPlayer.getCurrentPosition()
    }

    override fun getPlayerState(): PlayerState {
        return trackPlayer.playerState
    }

    override fun setTrackCompletionListener(listener: (PlayerState) -> Unit) {
        trackPlayer.setOnCompletionListener(listener)
    }

}