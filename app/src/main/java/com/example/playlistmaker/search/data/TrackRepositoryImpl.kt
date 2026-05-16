package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.Resource
import com.example.playlistmaker.search.data.dto.ResponseStatus
import com.example.playlistmaker.search.data.dto.TracksResponse
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.TracksRepository

class TracksRepositoryImpl(private val networkClient: NetworkClient) :
    TracksRepository {

    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))

        return when (response.status) {
            ResponseStatus.NO_INTERNET -> Resource.Error("Проверьте подключение к интернету")
            ResponseStatus.SUCCESS -> {
                Resource.Success((response as TracksResponse).results.map { dto ->
                    DtoToTrackMapper.map(
                        dto
                    )
                })
            }

            ResponseStatus.BAD_REQUEST -> Resource.Error("Неверный запрос")
            else -> Resource.Error("Ошибка сервера")
        }
    }
}