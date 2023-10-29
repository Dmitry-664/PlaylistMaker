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
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private lateinit var playerTime: TextView
    private lateinit var track: Track
    private var audioPlayerViewHolder: AudioPlayerViewHolder? = null
    private var playerState = STATE_DEFAULT
    private lateinit var mediaUri: Uri
    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var playAudioPlayer: ImageView

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

//    override fun onResume() {
//        super.onResume()
//        playAudioPlayer?.setOnClickListener {
//            playbackControl()
//            handler.post(conditionTime)
//        }
//    }

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
            playerState = STATE_PREPARED
        }
        mediaPlayer?.setOnCompletionListener {
            playAudioPlayer
            playerState = STATE_PREPARED
            playerTime?.text = getString(R.string.zero_time)
            handler.removeCallbacks(conditionTime())
        }
    }

    private fun startPlayer() {
        mediaPlayer?.start()
        playAudioPlayer?.setImageResource(R.drawable.ic_play_audio_player_checked)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer?.pause()
        playAudioPlayer?.setImageResource(R.drawable.ic_play_audio_player)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
                playAudioPlayer?.setImageResource(R.drawable.ic_play_audio_player)
                handler.removeCallbacks(conditionTime())
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
                playAudioPlayer?.setImageResource(R.drawable.ic_play_audio_player_checked)
                handler.post(conditionTime())
            }
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