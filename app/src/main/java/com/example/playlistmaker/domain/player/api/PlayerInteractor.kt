package com.example.playlistmaker.domain.player.api

interface PlayerInteractor {
    fun preparePlayer(previewUrl: String?)

    fun prepareAsync()

    fun startPlayer()
    fun playerGame(): Boolean

    fun pausePlayer()

    fun release()

    fun setOnPreparedListener(listener: (() -> Unit)?)

    fun setOnCompletionListener(listener: (() -> Unit)?)

    fun getCurrentPosition(): Int
}