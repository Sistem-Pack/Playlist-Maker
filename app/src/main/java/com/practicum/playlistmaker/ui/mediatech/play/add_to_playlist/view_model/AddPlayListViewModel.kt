package com.practicum.playlistmaker.ui.mediatech.play.add_to_playlist.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.Consts
import com.practicum.playlistmaker.domain.playlist.PlayListsInteractor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AddPlayListViewModel(private val playListsInteractor: PlayListsInteractor) : ViewModel() {

    private var isClickAllowed = true

    fun createPlayList(
        name: String,
        description: String,
        pickImageUri: Uri?,
        onResultListener: () -> Unit
    ) {
        viewModelScope.launch {
            playListsInteractor.addPlayList(name, description, pickImageUri)
            onResultListener()
        }
    }

    fun editPlayList(
        playListId: Int,
        name: String,
        description: String,
        pickImageUri: Uri?,
        onResultListener: () -> Unit
    ) {
        viewModelScope.launch {
            playListsInteractor.editPlayList(playListId, name, description, pickImageUri)
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

}