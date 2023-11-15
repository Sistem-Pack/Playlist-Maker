package com.practicum.playlistmaker.creator

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.data.contentprovider.impl.ContentProviderImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.settings.impl.SettingsRepository
import com.practicum.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.sharing.ExternalNavigator
import com.practicum.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.domain.api.ITrackInteractor
import com.practicum.playlistmaker.domain.api.ITracksRepository
import com.practicum.playlistmaker.domain.impl.TrackInteractorImpl
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import com.practicum.playlistmaker.data.contentprovider.ContentProvider

object Creator {
    private fun getTrackRepository(): ITracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun tracksInteractor(): ITrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(
            provideExternalNavigator(context),
            provideContentProvider(context)
        )
    }

    private fun provideContentProvider(context: Context): ContentProvider {
        return ContentProviderImpl(context)
    }

    private fun provideExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(getSettingsStorage(context))
    }

    private fun getSettingsStorage(context: Context): SettingsThemeStorage {
        return SharedPrefsSettingsThemeStorage(
            context.getSharedPreferences(
                ContentProviderImpl(context).getStringFromResources(""),
                AppCompatActivity.MODE_PRIVATE
            )
        )
    }

}