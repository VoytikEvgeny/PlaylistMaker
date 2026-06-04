package com.example.playlistmaker.search.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.SearchState
import com.example.playlistmaker.setting.domain.ConsumerData
import com.google.gson.Gson

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val gson: Gson
) : ViewModel() {

    private val state = MutableLiveData<SearchState>()
    fun getState(): LiveData<SearchState> = state

    fun loadData(searchTrack: String) {
        state.value = SearchState.Loading

        tracksInteractor.searchTracks(
            expression = searchTrack,
            consumer = object : TracksInteractor.TracksConsumer {

                override fun consume(data: ConsumerData<List<Track>>) {

                    when (data) {
                        is ConsumerData.Data -> {
                            val content = SearchState.Content(data.value)
                            state.postValue(content)
                        }

                        is ConsumerData.Error -> {
                            val error = SearchState.Error(data.message)
                            state.postValue(error)
                        }
                    }

                }

            }
        )

    }

    fun gson(): Gson {
        return gson
    }
}