package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.TracksSearchResponse
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.setting.domain.ApiResponse
import java.util.Objects

class TracksRepositoryImpl(private val networkClient: NetworkClient) :
    TracksRepository {

    override fun searchTracks(searchStr: String): ApiResponse<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(searchStr))
        if (response.resultCode == 200) {
            return ApiResponse.Success((response as TracksSearchResponse).results.filter {
                Objects.nonNull(
                    it
                )
            }.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            })
        } else {
            return ApiResponse.Error("response result code is ${response.resultCode}")
        }
    }
}