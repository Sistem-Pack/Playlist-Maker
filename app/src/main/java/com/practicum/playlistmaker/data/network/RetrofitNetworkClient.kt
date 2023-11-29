package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.search.dto.Response
import com.practicum.playlistmaker.data.search.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient() : NetworkClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private var iTunesService = retrofit.create(SearchApiService::class.java)
    override fun doRequest(dto: Any): Response {
        return if (dto is TrackSearchRequest) {
            try {
                val resp = iTunesService.search(dto.expression).execute()
                val body = resp.body() ?: Response()
                body.apply { resultCode = resp.code() }
            } catch (e: Exception) {
                Response().apply { resultCode = -1 }
            }
        } else {
            Response().apply { resultCode = 400 }
        }
    }
}