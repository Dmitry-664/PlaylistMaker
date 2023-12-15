package com.example.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.example.playlistmaker.domain.models.player.State
import com.example.playlistmaker.domain.player.api.PlayerRepository

class PlayerRepositoryImpl(private val mediaPlayer: MediaPlayer): PlayerRepository {
    private var playerState = State.DEFAULT
    override fun preparePlayer(previewUrl: String?) {
        mediaPlayer.apply { setDataSource(previewUrl) }
    }

    override fun prepareAsync() {
        mediaPlayer.prepareAsync()
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = State.PLAYING
    }

    override fun playerGame(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = State.PAUSED
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun setOnPreparedListener(listener: (() -> Unit)?) {
        mediaPlayer.setOnPreparedListener { listener?.invoke() }
    }

    override fun setOnCompletionListener(listener: (() -> Unit)?) {
        mediaPlayer.setOnCompletionListener { listener?.invoke() }
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }


}