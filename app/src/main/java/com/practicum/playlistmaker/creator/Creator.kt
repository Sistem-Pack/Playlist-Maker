package com.practicum.playlistmaker.creator

import com.practicum.playlistmaker.data.ExternalNavigatorR
import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.api.ITrackInteractor
import com.practicum.playlistmaker.domain.api.ITracksRepository
import com.practicum.playlistmaker.domain.impl.TrackInteractorImpl
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import com.practicum.playlistmaker.data.sharing.ExternalNavigator

object Creator {
    private fun getTrackRepository(): ITracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun tracksInteractor(): ITrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    fun provideSettingsInteractor(): SettingsInteractor {
      return SettingsInteractorImpl()
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(ExternalNavigatorR())
    }

}