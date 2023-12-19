package com.example.playlistmaker.ui.player

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.search.SOMETHING_KEY_TRACK
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.player.State
import com.google.gson.Gson

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var playerTime: TextView
    private lateinit var track: Track
    private lateinit var audioPlayerViewHolder: AudioPlayerViewHolder
    private lateinit var playAudioPlayer: ImageView
    private val viewModel by viewModels<AudioPlayerViewModel> {
        AudioPlayerViewModel.getViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        playerTime = findViewById(R.id.timeSongAudioPlayer)
        playAudioPlayer = findViewById(R.id.playAudioPlayer)
        val backButton: Button = findViewById(R.id.buttonBackAudioPlayer)
        val addToPlayList: ImageView = findViewById(R.id.addSongAudioPlayer)
        val likeButton: ImageView = findViewById(R.id.liveSongAudioPlayer)

        audioPlayerViewHolder = AudioPlayerViewHolder(this)

        track = getTrack(intent.getStringExtra(SOMETHING_KEY_TRACK))

        audioPlayerViewHolder!!.bind(track)
        viewModel.setPlayer(track.previewUrl!!)
        
        viewModel.audioState.observe(this) {
            buttonImage(it.playerState)
        }

        playAudioPlayer?.setOnClickListener {
            viewModel.playbackControl()
        }

        likeButton.setOnClickListener {
            likeButton.setImageResource(R.drawable.ic_live_song_checked)
        }

        addToPlayList.setOnClickListener {
            addToPlayList.setImageResource(R.drawable.ic_add_song_checked)
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun buttonImage(playerState: State) {
        when (playerState) {
            State.PLAYING -> playAudioPlayer?.setImageResource(R.drawable.ic_play_audio_player_checked)
            State.PAUSED -> playAudioPlayer?.setImageResource(R.drawable.ic_play_audio_player)
            State.PREPARED -> playAudioPlayer?.setImageResource(R.drawable.ic_play_audio_player)
            State.DEFAULT -> {}
        }

    }
    override fun onPause() {
        super.onPause()
        playAudioPlayer?.setImageResource(R.drawable.ic_play_audio_player)
        viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    private fun getTrack(json: String?) = Gson().fromJson(json, Track::class.java)

}

