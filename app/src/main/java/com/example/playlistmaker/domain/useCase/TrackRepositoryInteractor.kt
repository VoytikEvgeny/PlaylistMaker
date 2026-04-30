package com.example.playlistmaker.domain.useCase

import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.api.TrackRepositoryInteractorInterface
import com.example.playlistmaker.domain.model.Track

class TrackRepositoryInteractor(private val repository: TrackRepository):
    TrackRepositoryInteractorInterface {
    override fun execute(query: String, callback: (Result<List<Track>>) -> Unit) {
        repository.searchTracks(query, callback)
    }

    override fun destroy(){
        repository.cancelThread()
    }
}