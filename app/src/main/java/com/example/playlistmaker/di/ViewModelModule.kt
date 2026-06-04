@file:Suppress("DEPRECATION")

package com.example.playlistmaker.di

import android.media.MediaPlayer
import com.example.playlistmaker.player.ui.viewModel.PlayerViewModel
import com.example.playlistmaker.search.ui.viewModel.SearchViewModel
import com.example.playlistmaker.setting.ui.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        PlayerViewModel(get(), get())
    }
    viewModel {
        SearchViewModel(get(), get())
    }

    factory<MediaPlayer> {
        MediaPlayer()
    }
}