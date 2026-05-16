package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.search.domain.models.Track

class PlayerInteractorImpl(private val repository: PlayerRepository) : PlayerInteractor {

    override fun setCurrentTrack(track: Track) {
        return repository.setCurrentTrack(track)
    }

    override fun getCurrentTrack(): Track? {
        return repository.getCurrentTrack()
    }
}