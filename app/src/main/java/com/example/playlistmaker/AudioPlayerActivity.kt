package com.example.playlistmaker

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    enum class State {
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSED
    }

    private var playerTime: TextView? = null
    private lateinit var track: Track
    private var audioPlayerViewHolder: AudioPlayerViewHolder? = null
    private var playerState = State.DEFAULT
    private var mediaUri: Uri? = null
    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private var playAudioPlayer: ImageView? = null

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
        preparePlayer()

        playAudioPlayer?.setOnClickListener {
            playbackControl()
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
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

    private fun preparePlayer(){
        mediaUri = Uri.parse(track!!.previewUrl)
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(applicationContext, mediaUri!!)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener{
            playAudioPlayer?.isEnabled = true
            playerState = State.PREPARED
        }
        mediaPlayer?.setOnCompletionListener {
            playAudioPlayer
            playerState = State.PREPARED
            playerTime?.text = getString(R.string.zero_time)
            handler.removeCallbacks(conditionTime())
        }
    }

    private fun startPlayer() {
        mediaPlayer?.start()
        playAudioPlayer?.setImageResource(R.drawable.ic_play_audio_player_checked)
        playerState = State.PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer?.pause()
        playAudioPlayer?.setImageResource(R.drawable.ic_play_audio_player)
        playerState = State.PAUSED
    }

    private fun playbackControl() {
        when(playerState) {
            State.PLAYING -> {
                pausePlayer()
                playAudioPlayer?.setImageResource(R.drawable.ic_play_audio_player)
                handler.removeCallbacks(conditionTime())
            }
            State.PREPARED, State.PAUSED -> {
                startPlayer()
                playAudioPlayer?.setImageResource(R.drawable.ic_play_audio_player_checked)
                handler.post(conditionTime())
            }

            else -> Unit
        }
    }

    private fun conditionTime(): Runnable {
        return object : Runnable {
            override fun run() {
                val currentPosition = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer?.currentPosition)
                if (mediaPlayer?.isPlaying == true) {
                    playerTime?.text = currentPosition
                    handler.postDelayed(this, 10)
                }
            }
        }
    }

    private fun getTrack(json: String?) = Gson().fromJson(json, Track::class.java)

}