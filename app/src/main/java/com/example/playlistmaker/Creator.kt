package com.example.playlistmaker

import com.example.playlistmaker.data.dto.SharedPrefsImpl
import com.example.playlistmaker.data.dto.TrackHistoryManagerImpl
import com.example.playlistmaker.data.dto.TrackRepositoryImpl
import com.example.playlistmaker.data.mapper.TrackMapper
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.NetworkClient
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.SharedPrefs
import com.example.playlistmaker.domain.api.ThemeManager
import com.example.playlistmaker.domain.api.TrackHistoryManager
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.impl.TrackMapperImpl
import com.example.playlistmaker.domain.impl.TrackPlayerImpl
import com.example.playlistmaker.domain.useCase.TrackRepositoryInteractor

object Creator {
    fun getSharedPrefs(): SharedPrefs {
        return SharedPrefsImpl()
    }

    fun getTrackManager(): TrackHistoryManager {
        return TrackHistoryManagerImpl
    }

    fun getMapper(): TrackMapper {
        return TrackMapperImpl()
    }

    fun provideNetworkClient(): NetworkClient {
        return RetrofitNetworkClient()
    }

    fun provideTrackRepository(networkClient: NetworkClient): TrackRepository {
        return TrackRepositoryImpl(networkClient)
    }

    fun provideTrackUseCase(repository: TrackRepository): TrackRepositoryInteractor {
        return TrackRepositoryInteractor(repository)
    }

    fun getMediaPlayer(): PlayerInteractor {
        return TrackPlayerImpl()
    }

    fun getThemeManager(): ThemeManager {
        return SharedPrefsImpl()
    }
}