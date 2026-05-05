package com.example.playlistmaker.domain.api

import android.content.Context
import com.example.playlistmaker.data.dto.TrackDto

interface TrackHistoryManager {
    fun initializeHistory(context: Context)
    fun addTrackToHistory(track: TrackDto)
    fun deleteHistory(context: Context)
    fun getTrackHistory(): List<TrackDto>
    fun putLastTrack(track: TrackDto)
    fun getLastTrack(): TrackDto
}