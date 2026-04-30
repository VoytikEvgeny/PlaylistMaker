package com.example.playlistmaker.data.dto

import android.content.Context
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.api.TrackHistoryManager
import com.example.playlistmaker.historyListSize

object TrackHistoryManagerImpl : TrackHistoryManager {

    private var trackHistory = mutableListOf<TrackDto>()
    private lateinit var lastTrack: TrackDto
    private var sharedPrefs = Creator.getSharedPrefs()

    override fun initializeHistory(context: Context) {
        trackHistory = sharedPrefs.getHistory(context)
    }

    override fun addTrackToHistory(track: TrackDto) {

        if ((trackHistory.size < historyListSize) && !trackHistory.contains(track)) {
            trackHistory.add(0, track)
        } else if ((trackHistory.size >= historyListSize) && !trackHistory.contains(track)) {
            trackHistory.removeAt(historyListSize-1)
            trackHistory.add(0, track)
        } else if (trackHistory.contains(track)) {
            trackHistory.remove(track)
            trackHistory.add(0, track)
        }
    }

    override fun deleteHistory(context: Context) {
        trackHistory.clear()
    }

    override fun getTrackHistory(): List<TrackDto> {
        return trackHistory
    }

    override fun putLastTrack(track: TrackDto) {
        lastTrack = track
    }

    override fun getLastTrack(): TrackDto {
        return lastTrack
    }

}