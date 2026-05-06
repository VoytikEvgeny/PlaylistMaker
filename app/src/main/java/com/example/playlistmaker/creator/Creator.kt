package com.example.playlistmaker.creator

import android.app.Application
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.player.domain.TrackPlayer
import com.example.playlistmaker.player.data.TrackPlayerImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.domain.SearchHistoryRepository
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.data.TracksInteractorImpl
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.setting.data.SettingsRepositoryImpl
import com.example.playlistmaker.setting.domain.SettingsInteractor
import com.example.playlistmaker.setting.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.setting.domain.SettingsRepository
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.impl.SharingRepositoryImpl
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.data.SharingInteractor
import com.example.playlistmaker.sharing.data.impl.SharingInteractorImpl
import com.example.playlistmaker.sharing.domain.SharingRepository


object Creator {

    private lateinit var application: Application

    private val playerRepository = PlayerRepositoryImpl

    fun initApplication(application: Application) {
        Creator.application = application
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(application))
    }

    private fun getSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(application)
    }

    private fun getSettingsRepository() : SettingsRepository {
        return SettingsRepositoryImpl(application)
    }

    private fun getExternalNavigator() : ExternalNavigator {
        return ExternalNavigatorImpl()
    }

    private fun getSharingRepository() : SharingRepository {
        return SharingRepositoryImpl(application)
    }

    private fun getPlayerRepository() : PlayerRepository {
        return playerRepository
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(
            getSearchHistoryRepository()
        )
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(), getSharingRepository())
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    fun proviedTrackPlayer() : TrackPlayer {
        return TrackPlayerImpl(getPlayerRepository())
    }

}