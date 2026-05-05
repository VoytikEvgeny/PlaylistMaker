package com.example.playlistmaker.domain.impl

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.api.PlayerInteractor

class TrackPlayerImpl : PlayerInteractor {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private var secondsRemain: Int = 30
    private val handler = Handler(Looper.getMainLooper())
    private var isPlaying: Boolean = false
    private var remainingTime: Int = 0

    override fun prepare(trackUrl: String, timerRunnable: Runnable) {
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {
            isPlaying = false
            playerState = STATE_PREPARED
            secondsRemain =
                (mediaPlayer.duration / 1000).coerceAtMost(30)
        }

        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            isPlaying = false
            handler.removeCallbacks(timerRunnable)
            secondsRemain = (mediaPlayer.duration / 1000).coerceAtMost(30)
        }
    }

    override fun start() {
        mediaPlayer.start()
        isPlaying = true
        playerState = STATE_PLAYING
    }

    override fun getSecondsRemain(): Int {
        return if (isPlaying) {
            remainingTime = (mediaPlayer.duration - mediaPlayer.currentPosition) / 1000
            remainingTime.coerceIn(0, 30)
        } else {
            remainingTime
        }
    }

    override fun getCurrentState(): Boolean {
        return isPlaying
    }

    override fun pause() {
        mediaPlayer.pause()
        isPlaying = false
        playerState = STATE_PAUSED
    }

    override fun playbackControl(timerRunnable: Runnable) {
        when (playerState) {
            STATE_PLAYING -> {
                pause()
                handler.removeCallbacks(timerRunnable)
            }

            STATE_PAUSED, STATE_PREPARED -> {
                start()
                handler.post(timerRunnable)
            }
        }
    }

    override fun destroy() {
        mediaPlayer.release()
    }
}