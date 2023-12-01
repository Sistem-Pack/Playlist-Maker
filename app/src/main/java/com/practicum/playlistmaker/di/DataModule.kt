package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.creator.Consts
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.network.SearchApiService
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<SearchApiService> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchApiService::class.java)
    }

    single(named(Consts.USER_PREFS)) {
        /*androidContext()
            .getSharedPreferences(Consts.SEARCH_HISTORY, Context.MODE_PRIVATE)*/
    }

    single(named(Consts.USER_PREFS)) {
        /*androidContext()
            .getSharedPreferences(Consts.SW_MODE, Context.MODE_PRIVATE)*/
    }

    factory {
        Gson()
    }

    factory {
        MediaPlayer()
    }

    factory<NetworkClient> {
        RetrofitNetworkClient()
    }

    single(named("context")) {
        App()
    }



}