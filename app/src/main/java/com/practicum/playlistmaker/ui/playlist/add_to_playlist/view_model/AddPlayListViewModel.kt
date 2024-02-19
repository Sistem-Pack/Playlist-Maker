package com.practicum.playlistmaker.ui.playlist.add_to_playlist.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.playlist.PlayListsInteractor
import com.practicum.playlistmaker.domain.search.models.PlayList
import kotlinx.coroutines.launch

class AddPlayListViewModel(private val playListsInteractor: PlayListsInteractor) : ViewModel() {

    fun createPlayList(name: String, description: String, image: String?) {
        val playList = PlayList(
            playListId = 0,
            name = name,
            description = description,
            image = image,
            tracksCount = 0
        )
        viewModelScope.launch {
            playListsInteractor.addPlayList(playList)
        }
    }



}