package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.models.Track

interface PlayerRepository {
    fun setCurrentTrack(track: Track)
    fun getCurrentTrack(): Track
}