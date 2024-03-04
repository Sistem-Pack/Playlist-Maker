package com.practicum.playlistmaker.ui.playlist.play_lists_bottom_sheet.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.Consts
import com.practicum.playlistmaker.domain.playlist.PlayListsInteractor
import com.practicum.playlistmaker.domain.search.models.PlayList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PlayListBottomSheetViewModel(
    private val playListsInteractor: PlayListsInteractor
): ViewModel() {
    private var isClickAllowed = true
    val tracksCount: MutableLiveData<Int> = MutableLiveData(0)

    fun deletePlaylist(playList: PlayList, onResultListener: () -> Unit) {
        viewModelScope.launch {
            playListsInteractor.deletePlaylist(playList)
            onResultListener()
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

    fun observeTrackCount(playListId: Int) {
        viewModelScope.launch {
            playListsInteractor.getTrackCountForPlaylist(playListId)
                .collect { count ->
                    tracksCount.value = count
                }
        }
    }

}