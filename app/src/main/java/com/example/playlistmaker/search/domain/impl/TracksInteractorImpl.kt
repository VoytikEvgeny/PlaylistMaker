package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.setting.domain.ApiResponse
import com.example.playlistmaker.setting.domain.ConsumerData
import java.util.concurrent.Executor

class TracksInteractorImpl(
    private val repository: TracksRepository,
    private val executor: Executor
) : TracksInteractor {
    override fun searchTracks(searchStr: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {

            when (val searchResponse = repository.searchTracks(searchStr)) {
                is ApiResponse.Success -> {
                    consumer.consume(ConsumerData.Data(searchResponse.data))
                }

                is ApiResponse.Error -> {
                    consumer.consume(ConsumerData.Error("Что-то пошло не так ${searchResponse.message}"))
                }
            }

        }
    }
}