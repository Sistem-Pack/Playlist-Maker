package com.practicum.playlistmaker.creator

import android.content.Context
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.settings.SettingsRepository
import com.practicum.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.sharing.ExternalNavigator
import com.practicum.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.domain.search.api.TrackSearchInteractor
import com.practicum.playlistmaker.domain.search.api.TrackSearchRepository
import com.practicum.playlistmaker.domain.search.impl.TrackSearchInteractorImpl
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import com.practicum.playlistmaker.data.player.impl.TrackPlayerImpl
import com.practicum.playlistmaker.data.search.impl.TracksHistoryStorageImpl
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.player.TrackPlayer

object Creator {

    private lateinit var app: App
    fun initApp(application: App) {
        app = application
    }

    private fun getTrackRepository(): TrackSearchRepository {
        return TracksRepositoryImpl(
            RetrofitNetworkClient(),
            TracksHistoryStorageImpl(
                app.getSharedPreferences(Consts.SEARCH_HISTORY, Context.MODE_PRIVATE)
            )
        )
    }

    fun provideSearchInteractor(): TrackSearchInteractor {
        return TrackSearchInteractorImpl(getTrackRepository())
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(provideSettingsRepository())
    }

    fun provideSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl()
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(
            app,
            provideExternalNavigator())
    }

    private fun provideExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(app)
    }

    fun provideGetPlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(provideTrackPlayer())
    }

    private fun provideTrackPlayer(): TrackPlayer {
        return TrackPlayerImpl()
    }


}