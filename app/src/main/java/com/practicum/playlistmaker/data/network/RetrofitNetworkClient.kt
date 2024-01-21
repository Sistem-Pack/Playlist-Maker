package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.search.dto.Response
import com.practicum.playlistmaker.data.search.dto.TrackSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient() : NetworkClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private var iTunesService = retrofit.create(SearchApiService::class.java)
    override suspend fun doRequest(dto: TrackSearchRequest): Response {
        val resp = iTunesService.search(dto.expression).execute()
        return withContext(Dispatchers.IO) {
            try {
                val body = resp.body() ?: Response()
                body.apply { resultCode = resp.code() }
            } catch (e: Throwable) {
                Response().apply { resultCode = 400 }
            }
        }
        /*    return withContext(Dispatchers.IO) {
                try {
                    val resp = iTunesService.search(dto.expression).execute()
                    val body = resp.body() ?: Response()
                    body.apply { resultCode = resp.code() }
                } catch (e: Throwable) {
                    Response().apply { resultCode = 400 }
                }
            }*/
    }
}
            /*return if (dto is TrackSearchRequest) {
                    val resp = iTunesService.search(dto.expression).execute()
                    val body = resp.body() ?: Response()
                    body.apply { resultCode = resp.code() }
                } else {
                    Response().apply { resultCode = 400 }
                }*/
