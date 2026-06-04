package com.example.playlistmaker.player.ui.viewModel

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.ui.PlayerState
import com.example.playlistmaker.player.domain.StateData
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson

class PlayerViewModel(private val gson: Gson, private val mediaPlayer: MediaPlayer) : ViewModel() {
    private val state = MutableLiveData<PlayerState>()
    fun getState(): LiveData<PlayerState> = state
    fun fromJson(trackJson: String?, javaClass: Class<Track>): Track {
        return gson.fromJson(trackJson, javaClass)
    }

    fun preparePlayer(url: String?) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            state.postValue(PlayerState.Prepared(StateData(STATE_PREPARED)))
        }
        mediaPlayer.setOnCompletionListener {
            state.postValue(PlayerState.Completion(StateData(STATE_PREPARED)))
        }
    }

    fun currentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    fun startPlayer() {
        mediaPlayer.start()
        state.postValue(PlayerState.Start(StateData(STATE_PLAYING)))
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        state.postValue(PlayerState.Pause(StateData(STATE_PAUSED)))
    }

    fun releasePlayer() {
        mediaPlayer.release()
    }

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }
}