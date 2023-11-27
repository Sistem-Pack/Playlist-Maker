package com.practicum.playlistmaker.ui.player.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.contentprovider.ContentProvider

class PlayerViewModelFactory(private val contentProvider: ContentProvider,
) : ViewModelProvider.Factory {

    private val playerInteractor = Creator.provideGetPlayerInteractor()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlayerViewModel(
            playerInteractor = playerInteractor,
            contentProvider = contentProvider,
        ) as T
    }

}