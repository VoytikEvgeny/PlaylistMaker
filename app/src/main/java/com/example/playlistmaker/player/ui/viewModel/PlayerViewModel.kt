package com.example.playlistmaker.player.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.TrackPlayer
import com.example.playlistmaker.player.ui.PlayStatus
import com.example.playlistmaker.player.ui.TrackScreenState

class PlayerViewModel(private val trackPlayer: TrackPlayer) : ViewModel() {
    private var screenStateLiveData = MutableLiveData<TrackScreenState>(TrackScreenState.Loading)
    fun getScreenStateLiveData(): LiveData<TrackScreenState> = screenStateLiveData
    private val playStatusLiveData = MutableLiveData<PlayStatus>()
    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData

    init {
        trackPlayer.prepare { track ->
            screenStateLiveData.postValue(
                TrackScreenState.Content(track)
            )
        }
    }

    fun play() {
        trackPlayer.play(
            statusObserver = object : TrackPlayer.StatusObserver {
                override fun onProgress(progress: Float) {
                    playStatusLiveData.value = getCurrentPlayStatus().copy(
                        progress = formatTime(progress),
                    )
                }

                override fun onPause() {
                    playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = false)
                }

                override fun onPlay() {
                    playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = true)
                }

                override fun onCompletion() {
                    playStatusLiveData.value = PlayStatus(
                        progress = "00:00",
                        isPlaying = false,
                    )
                }
            },
        )
    }

    private fun formatTime(progress: Float): String {
        val seconds = progress.toInt()
        val minutes = seconds / 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun getCurrentPlayStatus(): PlayStatus {
        return playStatusLiveData.value ?: PlayStatus(progress = "00:00", isPlaying = false)
    }

    fun pause() {
        trackPlayer.pause()
    }

    override fun onCleared() {
        trackPlayer.release()
        super.onCleared()
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val trackPlayer = Creator.proviedTrackPlayer()
                PlayerViewModel(trackPlayer)
            }
        }
    }
}