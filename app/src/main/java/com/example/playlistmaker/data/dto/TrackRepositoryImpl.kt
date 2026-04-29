package com.example.playlistmaker.data.dto

import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.api.NetworkClient
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.model.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    private var searchThread: Thread? = null

    @Volatile
    private var isSearchCancelled = false

    override fun searchTracks(query: String, callback: (Result<List<Track>>) -> Unit) {
        isSearchCancelled = false
        searchThread?.interrupt()

        searchThread = Thread {
            val mapper = Creator.getMapper()
            val request = TracksSearchRequest(query)

            val response = networkClient.doRequest(request)

            if (response.resultCode == 200) {
                if (response is TrackSearchResponse) {
                    val result = response.results.map { mapper.map(it) }
                    callback(Result.success(result))
                }
            } else if (response.resultCode == 100) {
                callback(Result.success(emptyList()))
            } else {
                callback(Result.failure(Exception("Ошибка при загрузке данных")))
            }
        }.apply() { start() }
    }

    override fun canselThread() {
        isSearchCancelled = true
        searchThread?.interrupt()
        searchThread = null

    }
}