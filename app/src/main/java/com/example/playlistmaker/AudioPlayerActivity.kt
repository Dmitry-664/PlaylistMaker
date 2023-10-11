package com.example.playlistmaker

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var playerTime: TextView
    private lateinit var track: Track
    private var audioPlayerViewHolder: AudioPlayerViewHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        playerTime = findViewById(R.id.timeSongAudioPlayer)
        val backButton: ImageButton = findViewById(R.id.buttonBackAudioPlayer)
        val addToPlayList: ImageView = findViewById(R.id.addSongAudioPlayer)
        val playPause: ImageView = findViewById(R.id.playAudioPlayer)
        val likeButton: ImageView = findViewById(R.id.liveSongAudioPlayer)

        audioPlayerViewHolder = AudioPlayerViewHolder(this)

        track = getTrack(intent.getStringExtra(SOMETHING_KEY_TRACK))

        audioPlayerViewHolder!!.bind(track)

        backButton.setOnClickListener {
            finish()
        }

        playPause.setOnClickListener {
            playPause.setImageResource(R.drawable.ic_play_audio_player_checked)
        }

        likeButton.setOnClickListener {
            likeButton.setImageResource(R.drawable.ic_live_song_checked)
        }

        addToPlayList.setOnClickListener {
            addToPlayList.setImageResource(R.drawable.ic_add_song_checked)
        }

    }

    private fun getTrack(json: String?) = Gson().fromJson(json, Track::class.java)

}