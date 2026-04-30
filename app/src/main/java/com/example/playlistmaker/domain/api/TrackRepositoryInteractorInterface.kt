package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.Track

interface TrackRepositoryInteractorInterface {
    fun execute(query: String, callback: (Result<List<Track>>) -> Unit)
    fun destroy()
}