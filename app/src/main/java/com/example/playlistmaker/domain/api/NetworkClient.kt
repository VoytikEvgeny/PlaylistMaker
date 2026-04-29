package com.example.playlistmaker.domain.api

import com.example.playlistmaker.data.dto.Response


interface NetworkClient {
    fun doRequest(dto: Any): Response
}