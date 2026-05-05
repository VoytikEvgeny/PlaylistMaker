package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.models.Track

interface PlayerInteractor {
    fun setCurrentTrack(track: Track)
    fun getCurrentTrack(): Track?
}