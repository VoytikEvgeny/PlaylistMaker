package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.HISTORY_LIST_SIZE
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.SearchHistoryRepository
import com.example.playlistmaker.search.domain.models.Track

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    override fun getHistoryList(consumer: SearchHistoryInteractor.SearchHistoryConsumer) {
        val history = repository.getHistoryList().toMutableList()
        consumer.consume(history)
    }

    override fun saveHistoryList(list: List<Track>) {
        repository.saveHistoryList(list)
    }

    override fun addTrackToHistory(track: Track) {
        val trackList = repository.getHistoryList().toMutableList()
        val trackListIterator = trackList.iterator()
        while (trackListIterator.hasNext()) {
            if (trackListIterator.next().trackId == track.trackId)
                trackListIterator.remove()
        }
        trackList.add(0, track)
        if (trackList.size > HISTORY_LIST_SIZE) trackList.removeAt(HISTORY_LIST_SIZE - 1)
        saveHistoryList(trackList)
    }

    override fun clearSearchHistory() {
        saveHistoryList(emptyList<Track>())
    }
}