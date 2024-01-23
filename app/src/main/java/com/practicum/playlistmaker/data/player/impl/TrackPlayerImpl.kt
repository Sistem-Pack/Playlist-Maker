package com.practicum.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.player.TrackPlayer
import com.practicum.playlistmaker.domain.player.models.PlayerState

class TrackPlayerImpl : TrackPlayer {

    override var playerState: PlayerState = PlayerState.DEFAULT
    private var mediaPlayer = MediaPlayer()
    override fun preparePlayer(url: String, onChangeState: (s: PlayerState) -> Unit) {
        mediaPlayer.reset()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.PREPARED
            onChangeState(PlayerState.PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.DEFAULT
            onChangeState(PlayerState.DEFAULT)
        }
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.PAUSED
    }

    override fun releasePlayer() {
        mediaPlayer.release()
        playerState = PlayerState.DEFAULT
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun setOnCompletionListener(listener: (PlayerState) -> Unit) {
        when (playerState) {
            PlayerState.PLAYING -> {
                pausePlayer()
                listener(playerState)
            }

            PlayerState.PREPARED, PlayerState.PAUSED, PlayerState.DEFAULT -> {
                startPlayer()
                listener(playerState)
            }
        }
    }

}