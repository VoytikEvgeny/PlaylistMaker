package com.example.playlistmaker.domain.api

import android.content.Context
import com.example.playlistmaker.data.dto.TrackDto

interface SharedPrefs {
    fun saveHistory(history: List<TrackDto>, context: Context)
    fun getHistory(context: Context): MutableList<TrackDto>
}