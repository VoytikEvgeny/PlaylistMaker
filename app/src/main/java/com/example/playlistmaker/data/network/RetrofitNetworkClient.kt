package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.domain.api.NetworkClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient() : NetworkClient {
    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(SearchTrackApiService::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TracksSearchRequest) {
            val call = itunesService.search(dto.searchStr)

            return try {
                val response = call.execute()
                if (response.isSuccessful) {
                    val trackListDto = response.body()?.results ?: emptyList()
                    if (trackListDto.isEmpty()) {
                        Response().apply { resultCode = 100 }
                    } else {
                        TrackSearchResponse(
                            searchType = "search",
                            expression = dto.searchStr,
                            results = trackListDto
                        ).apply { resultCode = 200 }
                    }
                } else {
                    Response().apply { resultCode = 400 }
                }
            } catch (e: Exception) {
                Response().apply { resultCode = 400 }
            }
        }
        return Response().apply { resultCode = 400 }
    }
}