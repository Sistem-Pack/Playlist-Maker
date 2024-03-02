package com.practicum.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.Consts
import com.practicum.playlistmaker.Consts.TRACK_START_TIME
import com.practicum.playlistmaker.domain.favorite.FavoriteTracksInteractor
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.models.PlayerState
import com.practicum.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoriteInteractor: FavoriteTracksInteractor,
) : ViewModel() {

    private var timerJob: Job? = null
    var isFavorite = false

    private val _playState = MutableLiveData<PlayerState>()
    val playState: LiveData<PlayerState> = _playState

    private val _playDuration = MutableLiveData<String>()
    val playDuration: LiveData<String> = _playDuration

    private val _favoriteLiveData = MutableLiveData<Boolean>()
    val favoriteState: LiveData<Boolean> = _favoriteLiveData

    fun prepare(url: String) {
        playerInteractor.preparePlayer(url) { state ->
            when (state) {
                PlayerState.PREPARED -> {
                    _playState.postValue(PlayerState.PREPARED)
                    timerJob?.cancel()
                    _playDuration.postValue(TRACK_START_TIME)
                }

                PlayerState.DEFAULT -> {
                    _playState.postValue(PlayerState.DEFAULT)
                    timerJob?.cancel()
                    _playDuration.postValue(TRACK_START_TIME)
                }

                else -> Unit
            }
        }
    }

    private fun startTimer() {
        if (_playState.value == PlayerState.DEFAULT ||
            _playState.value == PlayerState.PREPARED) {
            _playDuration.postValue(TRACK_START_TIME)
        }
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(Consts.DELAY_MILLIS)
                _playDuration.postValue(SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(playerInteractor.getCurrentPosition()))
            }
        }
    }

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

    fun pausePlayer() {
        if (_playState.value == PlayerState.PLAYING) {
            timerJob?.cancel()
            playerInteractor.pausePlayer()
            _playState.postValue(PlayerState.PAUSED)
        }
    }

    fun resumePlayer() {
        if (_playState.value == PlayerState.PLAYING ||
            _playState.value == PlayerState.PAUSED) {
            timerJob?.cancel()
            playerInteractor.pausePlayer()
            _playState.postValue(PlayerState.PAUSED)
        }
    }

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

    fun checkIsFavorite(trackId: Int): Boolean {
        val idsList: MutableList<Int> = mutableListOf()
        viewModelScope.launch {
            favoriteInteractor
                .getIdsTracks()
                .collect { list ->
                    idsList.addAll(list)
                }
        }
        return if (idsList.contains(trackId)) {
            _favoriteLiveData.postValue(true)
            isFavorite = true
            true
        } else {
            _favoriteLiveData.postValue(false)
            isFavorite = false
            false
        }
    }

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            if (track.isFavorite) {
                favoriteInteractor.deleteTrack(track)
                _favoriteLiveData.postValue(false)
                track.isFavorite = false
                isFavorite = false
            } else {
                favoriteInteractor.insertTrack(track)
                _favoriteLiveData.postValue(true)
                track.isFavorite = true
                isFavorite = true
            }
        }
    }

}