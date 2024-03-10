package com.practicum.playlistmaker.ui.playlist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.Consts
import com.practicum.playlistmaker.data.sharing.ExternalNavigator
import com.practicum.playlistmaker.domain.playlist.PlayListsInteractor
import com.practicum.playlistmaker.ui.player.PlayListState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayListViewModel(
    private val externalNavigator: ExternalNavigator,
    private val playListsInteractor: PlayListsInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlayListState>()
    fun observeState(): LiveData<PlayListState> = stateLiveData

    private var isClickAllowed = true

    private fun renderState(state: PlayListState) {
        stateLiveData.postValue(state)
    }

    fun requestPlayListInfo(playListId: Int) {
        viewModelScope.launch {
            renderState(
                PlayListState.PlayListInfo(
                    playListsInteractor.getPlayList(playListId)
                )
            )
            delay(100)
            renderState(
                PlayListState.PlayListTracks(
                    playListsInteractor.getPlayListTracks(playListId).toMutableList()
                )
            )
        }
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(Consts.CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    fun shareText(text: String) {
        externalNavigator.shareText(text)
    }

    fun deleteTrackFromPlaylist(trackId: Int, playListId: Int) {
        viewModelScope.launch {
            playListsInteractor.deleteTrackFromPlaylist(trackId, playListId)
            renderState(
                PlayListState.PlayListTracks(
                    playListsInteractor.getPlayListTracks(playListId)
                )
            )
        }
    }

}