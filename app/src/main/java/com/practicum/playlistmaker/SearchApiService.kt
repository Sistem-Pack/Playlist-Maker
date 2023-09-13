package com.practicum.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {
    @GET("/search?entity=song")
    //fun search(): Call<List<Track>>
    fun search(@Query("term") text: String): Call<TrackResponse>

}