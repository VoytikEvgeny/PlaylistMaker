package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track

interface SearchHistoryRepository {
    fun getHistoryList(): Array<Track>
    fun saveHistoryList(list: List<Track>)
}