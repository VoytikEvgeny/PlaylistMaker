package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.Resource
import com.example.playlistmaker.search.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}