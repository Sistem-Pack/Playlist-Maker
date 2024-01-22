package com.practicum.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.player.models.PlayerState
import com.practicum.playlistmaker.domain.player.TrackPlayer

class TrackPlayerImpl : TrackPlayer {

    override var playerState: PlayerState = PlayerState.DEFAULT
    private var mediaPlayer = MediaPlayer()

    override fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepare()
        playerState = PlayerState.PREPARED
    }

    override fun startPlayer(url: String) {
        if (playerState == PlayerState.DEFAULT) {
            preparePlayer(url)
        }
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
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.PREPARED
            //listener.invoke(listener)
        }
    }

}