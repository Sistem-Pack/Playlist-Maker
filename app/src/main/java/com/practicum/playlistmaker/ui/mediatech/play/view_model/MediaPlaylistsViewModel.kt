package com.practicum.playlistmaker.ui.mediatech.play.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.playlist.PlayListsInteractor
import com.practicum.playlistmaker.ui.playlist.PlayListsState
import kotlinx.coroutines.launch

class MediaPlaylistsViewModel(private val playListsInteractor: PlayListsInteractor): ViewModel() {
    private val playListsStateLiveData = MutableLiveData<PlayListsState>()
    fun observePlayListsState(): LiveData<PlayListsState> = playListsStateLiveData

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
}
