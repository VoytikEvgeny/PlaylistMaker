package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.SearchHistoryRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.setting.data.HISTORY_LIST_KEY
import com.google.gson.Gson

class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
) : SearchHistoryRepository {
    override fun getHistoryList(): Array<Track> {
        val json = sharedPreferences.getString(HISTORY_LIST_KEY, null) ?: return emptyArray()
        return gson.fromJson(json, Array<Track>::class.java)
    }

    override fun saveHistoryList(list: List<Track>) {
        sharedPreferences.edit()
            .putString(HISTORY_LIST_KEY, createJsonFromHistoryList(list))
            .commit()
    }

    private fun createJsonFromHistoryList(historyList: List<Track>): String {
        return gson.toJson(historyList)
    }
}