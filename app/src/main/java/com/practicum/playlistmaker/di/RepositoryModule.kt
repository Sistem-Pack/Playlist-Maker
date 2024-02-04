package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.domain.favorite.FavoriteTracksRepository
import com.practicum.playlistmaker.Consts
import com.practicum.playlistmaker.data.favorite.impl.FavoriteTracksRepositoryImpl
import com.practicum.playlistmaker.data.player.impl.TrackPlayerImpl
import com.practicum.playlistmaker.data.search.TracksHistoryStorage
import com.practicum.playlistmaker.data.search.impl.TracksHistoryStorageImpl
import com.practicum.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.practicum.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.domain.player.TrackPlayer
import com.practicum.playlistmaker.domain.search.api.TrackSearchRepository
import com.practicum.playlistmaker.domain.settings.SettingsRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

var repositoryModule = module {

    factory<TrackPlayer> {
        TrackPlayerImpl()
    }

    factory<TracksHistoryStorage> {
        TracksHistoryStorageImpl(get(named(Consts.HISTORY_PREFS)))
    }

    factory<TrackSearchRepository> {
        TracksRepositoryImpl(get(), get())
    }

    factory<SettingsRepository> {
        SettingsRepositoryImpl(get(named(Consts.THEME_PREFS)))
    }

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(get(), get())
    }

}
