package com.example.playlistmaker

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPrefs: SharedPreferences) {
    private val MAX_HISTORY_SIZE = 10
    val tracks = ArrayList<Track>()
    private val gson: Gson = Gson()

    init {
        val json = sharedPrefs.getString(TRACK_HISTORY, ArrayList<Track>().toString())
        val type = object : TypeToken<List<Track>>() {}.type
        tracks.addAll(gson.fromJson(json, type))
    }

    fun addToHistory(track: Track) {
        if (tracks.contains(track)) {
            tracks.remove(track)
        }
        tracks.add(0, track)

        while (tracks.size > MAX_HISTORY_SIZE) {
            tracks.removeAt(MAX_HISTORY_SIZE)
        }
        reloadSharedPreferences()
    }

    private fun reloadSharedPreferences() {
        val json = gson.toJson(tracks)
        sharedPrefs.edit {
            putString(TRACK_HISTORY, json)
        }
    }

    fun clearHistory() {
        tracks.clear()
        reloadSharedPreferences()
    }
}