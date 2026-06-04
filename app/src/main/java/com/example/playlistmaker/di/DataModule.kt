package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.APPLICATION_PREFERENCES
import com.example.playlistmaker.I_TUNES_BASE_URL
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.network.IMDbApiService
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<IMDbApiService> {
        Retrofit.Builder()
            .baseUrl(I_TUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IMDbApiService::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences(APPLICATION_PREFERENCES, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }
}