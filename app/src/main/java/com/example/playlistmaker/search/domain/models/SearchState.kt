package com.example.playlistmaker.search.domain.models

sealed interface SearchState {

    data object Loading : SearchState
    data class Error(val message: String) : SearchState
    data class Content(val data: List<Track>) : SearchState

}