package com.example.playlistmaker.domain.api


interface PlayerInteractor {

    fun playbackControl(timerRunnable: Runnable)
    fun prepare(trackUrl: String, timerRunnable: Runnable)
    fun getCurrentState(): Boolean
    fun getSecondsRemain(): Int
    fun destroy()
    fun start()
    fun pause()
}