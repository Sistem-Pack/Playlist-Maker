package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.ui.mediatech.view_model.MediaFavoriteTracksViewModel
import com.practicum.playlistmaker.ui.mediatech.view_model.MediaPlaylistsViewModel
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModel = module {

    viewModel {
        PlayerViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(),get())
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        MediaFavoriteTracksViewModel()
    }

    viewModel {
        MediaPlaylistsViewModel()
    }

    viewModel {
        MediaFavoriteTracksViewModel(get())
    }

}