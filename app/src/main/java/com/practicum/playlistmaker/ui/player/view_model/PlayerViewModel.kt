package com.practicum.playlistmaker.ui.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.Consts
import com.practicum.playlistmaker.Consts.TRACK_START_TIME
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.models.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
) : ViewModel() {

    private var timerJob: Job? = null
    private val _playState = MutableLiveData<PlayerState>()
    val playState: LiveData<PlayerState> = _playState

    private val _playDuration = MutableLiveData<String>()
    val playDuration: LiveData<String> = _playDuration

    /*
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            _playDuration.postValue(
                SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(playerInteractor.getCurrentPosition())
            )
            handler.postDelayed(this, Consts.PLAY_TRACK_UPDATE_DELAY)
        }
    }
    */

    /*private fun startPlayer(url: String) {
        playerInteractor.startPlayer(url)
        handler.post(runnable)
        _playState.value = true
    }*/

    fun startPlayer() {
        if (playState.value == PlayerState.PREPARED ||
            playState.value == PlayerState.DEFAULT
        ) {
            _playDuration.postValue(SimpleDateFormat("mm:ss", Locale.getDefault())
                .format(0L))
        } else {
            _playDuration.postValue(SimpleDateFormat("mm:ss", Locale.getDefault())
                .format(playerInteractor.getCurrentPosition()))
            _playState.postValue(PlayerState.PLAYING)
            startTimer()
        }
    }

    private fun startTimer() {
        if (_playState.value == PlayerState.DEFAULT ||
            _playState.value == PlayerState.PREPARED) {
            _playDuration.postValue(Consts.TRACK_START_TIME)
        }
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(Consts.DELAY_MILLIS)
                _playDuration.postValue(SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(playerInteractor.getCurrentPosition()))
            }
        }
    }

    /*fun pausePlayer() {
        playerInteractor.pausePlayer()
        handler.removeCallbacks(runnable)
        _playState.value = false
    }*/

    fun pausePlayer() {
        if (_playState.value == PlayerState.PLAYING) {
            timerJob?.cancel()
            playerInteractor.pausePlayer()
            _playState.postValue(PlayerState.PAUSED)
        }
    }

    fun onResume() {
        if (_playState.value == PlayerState.PLAYING ||
            _playState.value == PlayerState.PAUSED) {
            timerJob?.cancel()
            playerInteractor.pausePlayer()
            _playState.postValue(PlayerState.PAUSED)
        }
    }

    override fun onCleared() {
        /*playerInteractor.releasePlayer()
        _playState.value = false*/
        super.onCleared()
    }

    /*fun playbackControl(url: String) {
        when (playerInteractor.getPlayerState()) {
            PlayerState.PLAYING -> {
                pausePlayer()
            }

            PlayerState.PAUSED, PlayerState.PREPARED -> {
                startPlayer(url)
            }

            PlayerState.DEFAULT -> {
                startPlayer(url)
                playerInteractor.setTrackCompletionListener {
                    _playState.value = false
                    _playDuration.value = Consts.TRACK_START_TIME
                    handler.removeCallbacks(runnable)
                }
            }
        }
    }*/

    fun changePlayerState() {
        playerInteractor.setTrackCompletionListener { state ->
            when (state) {
                PlayerState.PLAYING -> {
                    startTimer()
                    _playDuration.postValue(SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(playerInteractor.getCurrentPosition()))
                    _playState.postValue(PlayerState.PLAYING)
                }

                PlayerState.PAUSED -> {
                    _playState.postValue(PlayerState.PAUSED)
                    timerJob?.cancel()
                }

                PlayerState.PREPARED -> {
                    timerJob?.cancel()
                    _playDuration.postValue(TRACK_START_TIME)
                    _playState.postValue(PlayerState.PREPARED)
                }

                PlayerState.DEFAULT -> {
                    timerJob?.cancel()
                    _playDuration.postValue(TRACK_START_TIME)
                    _playState.postValue(PlayerState.DEFAULT)

                }
            }
        }
    }



}