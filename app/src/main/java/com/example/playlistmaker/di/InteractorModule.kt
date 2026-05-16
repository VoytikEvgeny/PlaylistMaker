package com.example.playlistmaker.di

import com.example.playlistmaker.player.data.TrackPlayerImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.TrackPlayer
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.search.data.TracksInteractorImpl
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.setting.domain.SettingsInteractor
import com.example.playlistmaker.setting.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module
import java.util.concurrent.Executor
import java.util.concurrent.Executors

val interactorModule = module {

    single<TracksInteractor> {
        TracksInteractorImpl(get(),get())
    }
    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    single<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    single<TrackPlayer> {
        TrackPlayerImpl(get(),get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }
    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<Executor> {
        Executors.newCachedThreadPool()
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }

}