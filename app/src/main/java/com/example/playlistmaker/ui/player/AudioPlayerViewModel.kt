package com.example.playlistmaker.ui.player

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.player.AudioPlayerState
import com.example.playlistmaker.domain.models.player.State
import com.example.playlistmaker.domain.player.api.PlayerInteractor
import com.example.playlistmaker.util.Creator

class AudioPlayerViewModel(private val playerInteractor: PlayerInteractor) : ViewModel() {
    private val audioStateLD = MutableLiveData(
        AudioPlayerState(
            playerState = State.DEFAULT,
            playerTime = 0
        )
    )
    val audioState: LiveData<AudioPlayerState> = audioStateLD

    private val handler = Handler(Looper.getMainLooper())
    private val conditionTime = object : Runnable {
        override fun run() {
            audioStateLD.postValue(
                audioStateLD.value?.copy(
                    playerTime = playerInteractor.getCurrentPosition()
                )
            )
            handler.postDelayed(this, TIME_DELAY)
        }
    }

    private fun setState(playerState: State) {
        audioStateLD.postValue(audioState.value?.copy(playerState = playerState))
    }

    private fun setTimer() {
        audioStateLD.postValue(
            audioStateLD.value?.copy(
                playerState = State.PREPARED,
                playerTime = 0
            )
        )
    }

    fun onPause() {
        playerInteractor.pausePlayer()
        setState(playerState = State.PAUSED)
        handler.removeCallbacks(conditionTime)
    }

    fun onDestroy() {
        setState(playerState = State.DEFAULT)
        playerInteractor.release()
        handler.removeCallbacks(conditionTime)
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
    }

    fun setPlayer(trackPreviewUrl: String) {
        playerInteractor.preparePlayer(trackPreviewUrl)
        playerInteractor.prepareAsync()
        playerInteractor.setOnPreparedListener { setState(State.PREPARED) }
        playerInteractor.setOnCompletionListener {
            handler.removeCallbacks(conditionTime)
            setTimer()
        }
    }

    fun playbackControl() {
        if (playerInteractor.playerGame()) {
            playerInteractor.pausePlayer()
            setState(State.PAUSED)
            handler.removeCallbacks(conditionTime)
        } else {
            playerInteractor.startPlayer()
            setState(State.PLAYING)
            handler.post(conditionTime)
        }
    }
    companion object{
        private const val TIME_DELAY = 200L

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val playerInteractor = Creator.providePlayerInteractor(MediaPlayer())
                AudioPlayerViewModel(playerInteractor)
            }
        }
    }
}