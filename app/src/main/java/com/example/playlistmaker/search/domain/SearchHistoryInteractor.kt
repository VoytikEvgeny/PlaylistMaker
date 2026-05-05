package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track

interface SearchHistoryInteractor {
    fun getHistoryList(consumer: SearchHistoryConsumer)
    fun saveHistoryList(list: List<Track>)
    fun addTrackToHistory(track: Track)
    fun clearSearchHistory()
    interface SearchHistoryConsumer {
        fun consume(history: MutableList<Track>)
    }
}