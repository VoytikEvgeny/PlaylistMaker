package com.example.playlistmaker.player.data

import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.search.domain.models.Track

object PlayerRepositoryImpl : PlayerRepository {

    private var currentTrack: Track? = null

    override fun setCurrentTrack(track: Track) {
        currentTrack = track
    }

    override fun getCurrentTrack(): Track = currentTrack!!

}