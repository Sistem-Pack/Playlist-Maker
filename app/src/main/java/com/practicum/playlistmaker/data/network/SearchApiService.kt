package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.search.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackSearchResponse>

}