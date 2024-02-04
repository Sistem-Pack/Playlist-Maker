package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.Consts
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.network.SearchApiService
import com.practicum.playlistmaker.data.sharing.ExternalNavigator
import com.practicum.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.domain.favorite.FavoriteTracksInteractor
import com.practicum.playlistmaker.domain.favorite.impl.FavoriteTracksInteractorImpl
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.search.api.TrackSearchInteractor
import com.practicum.playlistmaker.domain.search.impl.TrackSearchInteractorImpl
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors


val interactorModule = module {

    single<SearchApiService> {
        Retrofit.Builder().baseUrl(Consts.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchApiService::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(searchApiService = get())
    }

    factory<TrackSearchInteractor>{
        TrackSearchInteractorImpl(get(), get())
    }

    factory<ExternalNavigator> {
        ExternalNavigatorImpl(androidApplication())
    }

    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(androidApplication(), get())
    }

    factory {
        Executors.newCachedThreadPool()
    }

    single<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(get())
    }


}