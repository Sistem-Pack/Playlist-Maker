package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.Response

interface INetworkClient {
    fun doRequest(dto: Any): Response
}