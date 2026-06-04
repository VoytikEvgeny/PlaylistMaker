package com.example.playlistmaker.search.ui

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.HISTORY_LIST_SIZE
import com.example.playlistmaker.TRACK_HISTORY
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryService(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {

    val tracks = ArrayList<Track>()

    init {
        val json = sharedPreferences.getString(TRACK_HISTORY, ArrayList<Track>().toString())
        val type = object : TypeToken<List<Track>>() {}.type
        tracks.addAll(gson.fromJson(json, type))
    }

    fun addToHistory(track: Track) {
        if (tracks.contains(track)) {
            tracks.remove(track)
        }
        tracks.add(0, track)

        while (tracks.size > HISTORY_LIST_SIZE) {
            tracks.removeAt(HISTORY_LIST_SIZE)
        }
        reloadSharedPreferences()

    }

    private fun reloadSharedPreferences() {
        val json = gson.toJson(tracks)
        sharedPreferences.edit {
            putString(TRACK_HISTORY, json)
        }
    }

    fun clearHistory() {
        tracks.clear()
        reloadSharedPreferences()
    }
}