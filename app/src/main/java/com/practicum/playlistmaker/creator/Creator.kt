package com.practicum.playlistmaker.creator

import android.content.Context
import com.practicum.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.practicum.playlistmaker.data.contentprovider.impl.ContentProviderImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.settings.impl.SettingsRepository
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
import com.practicum.playlistmaker.data.contentprovider.ContentProvider
import com.practicum.playlistmaker.data.player.impl.TrackPlayerImpl
import com.practicum.playlistmaker.data.search.ShowPlayerInteractor
import com.practicum.playlistmaker.data.search.impl.ShowPlayerInteractorImpl
import com.practicum.playlistmaker.data.search.impl.TracksHistoryStorageImpl
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.api.TrackPlayer

object Creator {

    private fun getTrackRepository(context: Context): TrackSearchRepository {
        return TracksRepositoryImpl(
            RetrofitNetworkClient(context),
            TracksHistoryStorageImpl(
                context.getSharedPreferences(Consts.SEARCH_HISTORY, Context.MODE_PRIVATE)
            )
        )
    }

    fun provideSearchInteractor(context: Context): TrackSearchInteractor {
        return TrackSearchInteractorImpl(getTrackRepository(context))
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(provideSettingsRepository(context))
    }

    fun provideSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }

    fun provideShowPlayerInteractor(context: Context): ShowPlayerInteractor {
        return ShowPlayerInteractorImpl(context, provideContentProvider(context))
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(
            provideExternalNavigator(context),
            provideContentProvider(context)
        )
    }

    fun provideContentProvider(context: Context): ContentProvider {
        return ContentProviderImpl(context)
    }

    private fun provideExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    fun provideGetPlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(provideTrackPlayer())
    }

    private fun provideTrackPlayer(): TrackPlayer{
        return TrackPlayerImpl()
    }

}