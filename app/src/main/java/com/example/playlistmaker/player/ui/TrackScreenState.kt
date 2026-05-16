package com.example.playlistmaker.player.ui

import com.example.playlistmaker.search.domain.models.Track

sealed interface TrackScreenState {
    object Loading: TrackScreenState
    data class Content(
        val trackModel: Track
    ): TrackScreenState
}