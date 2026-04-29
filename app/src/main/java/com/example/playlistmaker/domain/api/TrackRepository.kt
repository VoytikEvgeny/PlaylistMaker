package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.Track


interface TrackRepository {
    fun searchTracks(query: String, callback: (Result<List<Track>>) -> Unit)
    fun canselThread()
}