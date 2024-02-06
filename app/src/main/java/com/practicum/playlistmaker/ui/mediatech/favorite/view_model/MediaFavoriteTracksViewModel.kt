package com.practicum.playlistmaker.ui.mediatech.favorite.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.Consts
import com.practicum.playlistmaker.domain.favorite.FavoriteTracksInteractor
import com.practicum.playlistmaker.domain.search.models.Track
import com.practicum.playlistmaker.ui.mediatech.favorite.FavoriteTrackState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MediaFavoriteTracksViewModel(
    private val interactor: FavoriteTracksInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<FavoriteTrackState>()
    fun observeState(): LiveData<FavoriteTrackState> = stateLiveData
    private var isClickAllowed = true

    fun waitData() {
        renderState(FavoriteTrackState.Loading)
        viewModelScope.launch {
            interactor
                .getTracks()
                .collect { tracks ->
                    processResult(tracks)
                }
        }
    }

    private fun processResult(tracks: List<Track>) {
        val actualState = if (tracks.isEmpty()) {
            FavoriteTrackState.Empty
        } else {
            FavoriteTrackState.Content(tracks)
        }
        renderState(actualState)
    }

    private fun renderState(state: FavoriteTrackState) {
        stateLiveData.postValue(state)
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            viewModelScope.launch {
                delay(Consts.CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }
}