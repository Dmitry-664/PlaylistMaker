package com.example.playlistmaker.domain.player.impl


import com.example.playlistmaker.domain.player.api.PlayerInteractor
import com.example.playlistmaker.domain.player.api.PlayerRepository

class PlayerInteractorImpl(private val playerRepository: PlayerRepository) : PlayerInteractor {
    override fun preparePlayer(previewUrl: String?) {
        playerRepository.preparePlayer(previewUrl)
    }

    override fun prepareAsync() {
        playerRepository.prepareAsync()
    }

    override fun startPlayer() {
        playerRepository.startPlayer()
    }

    override fun playerGame(): Boolean {
        return playerRepository.playerGame()
    }

    override fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    override fun release() {
        playerRepository.release()
    }

    override fun setOnPreparedListener(listener: (() -> Unit)?) {
        playerRepository.setOnPreparedListener(listener)
    }

    override fun setOnCompletionListener(listener: (() -> Unit)?) {
        playerRepository.setOnCompletionListener(listener)
    }

    override fun getCurrentPosition(): Int {
        return playerRepository.getCurrentPosition()
    }

}