package com.practicum.playlistmaker.ui.player.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.data.contentprovider.ContentProvider
import com.practicum.playlistmaker.data.settings.SettingsRepository
import com.practicum.playlistmaker.domain.search.models.Track

class PlayerViewModelFactory(
    private val track: Track,
    private val contentProvider: ContentProvider,
    private val context: Context
) : ViewModelProvider.Factory {

    private val playerInteractor = Creator.provideGetPlayerInteractor()
    private val settingsRepository: SettingsRepository =
        Creator.provideSettingsRepository(context = context)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlayerViewModel(
            playerInteractor = playerInteractor,
            contentProvider = contentProvider,
            settingsRepository = settingsRepository
        ) as T
    }

}