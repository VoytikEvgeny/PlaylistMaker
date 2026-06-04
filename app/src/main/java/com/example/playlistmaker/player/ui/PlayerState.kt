package com.example.playlistmaker.player.ui

import com.example.playlistmaker.player.domain.StateData

sealed class PlayerState(val stateData: StateData) {
    data class Prepared(val data: StateData) : PlayerState(data)
    data class Completion(val data: StateData) : PlayerState(data)
    data class Start(val data: StateData) : PlayerState(data)
    data class Pause(val data: StateData) : PlayerState(data)
}