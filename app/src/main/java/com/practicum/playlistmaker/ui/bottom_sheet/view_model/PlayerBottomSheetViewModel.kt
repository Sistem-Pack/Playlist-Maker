package com.practicum.playlistmaker.ui.bottom_sheet.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.Consts
import com.practicum.playlistmaker.domain.playlist.PlayListsInteractor
import com.practicum.playlistmaker.domain.playlist.models.PlayListsState
import com.practicum.playlistmaker.domain.search.models.PlayList
import com.practicum.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerBottomSheetViewModel(
    private val playListsInteractor: PlayListsInteractor
): ViewModel() {
    private val playListsStateLiveData = MutableLiveData<PlayListsState>()

    fun observePlayListsState(): LiveData<PlayListsState> = playListsStateLiveData

    private var isClickAllowed = true

    fun requestPlayLists() {
        viewModelScope.launch {
            val playLists = playListsInteractor.getPlayLists()
            if (playLists.isEmpty()) {
                playListsStateLiveData.postValue(PlayListsState.Empty)
            } else {
                playListsStateLiveData.postValue(PlayListsState.PlayLists(playLists))
            }
        }
    }

    fun addTrackToPlayList(track: Track, playList: PlayList) {
        viewModelScope.launch {
            if (playListsInteractor.isTrackInPlayList(track.trackId, playList.playListId)) {
                playListsStateLiveData.postValue(
                    PlayListsState.AddTrackToPlayListResult(false, playListName = playList.name)
                )
            } else {
                playListsInteractor.addTrackToPlayList(track, playList.playListId)
                playListsStateLiveData.postValue(
                    PlayListsState.AddTrackToPlayListResult(true, playListName = playList.name)
                )
            }
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

}