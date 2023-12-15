package com.example.playlistmaker.ui.player

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.search.SOMETHING_KEY_TRACK
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.player.State
import com.example.playlistmaker.ui.search.SearchViewModel
import com.google.gson.Gson
import java.util.Locale

class AudioPlayerActivity : ComponentActivity() {

    private var playerTime: TextView? = null
    private lateinit var track: Track
    private var audioPlayerViewHolder: AudioPlayerViewHolder? = null
//    private var playerState = State.DEFAULT
//    private var mediaUri: Uri? = null
//    private var mediaPlayer: MediaPlayer? = null
//    private val handler = Handler(Looper.getMainLooper())
    private var playAudioPlayer: ImageView? = null
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

//    private fun preparePlayer(){
//        mediaUri = Uri.parse(track!!.previewUrl)
//        mediaPlayer = MediaPlayer()
//        mediaPlayer?.setDataSource(applicationContext, mediaUri!!)
//        mediaPlayer?.prepareAsync()
//        mediaPlayer?.setOnPreparedListener{
//            playAudioPlayer?.isEnabled = true
//            playerState = State.PREPARED
//        }
//        mediaPlayer?.setOnCompletionListener {
//            playAudioPlayer
//            playerState = State.PREPARED
//            playerTime?.text = getString(R.string.zero_time)
//            handler.removeCallbacks(conditionTime())
//        }
//    }

//    private fun startPlayer() {
//        mediaPlayer?.start()
//        playAudioPlayer?.setImageResource(R.drawable.ic_play_audio_player_checked)
//        playerState = State.PLAYING
//    }

//    private fun pausePlayer() {
//        mediaPlayer?.pause()
//        playAudioPlayer?.setImageResource(R.drawable.ic_play_audio_player)
//        playerState = State.PAUSED
//    }

//    private fun playbackControl() {
//        when(playerState) {
//            State.PLAYING -> {
//                pausePlayer()
//                playAudioPlayer?.setImageResource(R.drawable.ic_play_audio_player)
//                handler.removeCallbacks(conditionTime())
//            }
//            State.PREPARED, State.PAUSED -> {
//                startPlayer()
//                playAudioPlayer?.setImageResource(R.drawable.ic_play_audio_player_checked)
//                handler.post(conditionTime())
//            }
//
//            else -> Unit
//        }
//    }

//    private fun conditionTime(): Runnable {
//        return object : Runnable {
//            override fun run() {
//                val currentPosition = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer?.currentPosition)
//                if (mediaPlayer?.isPlaying == true) {
//                    playerTime?.text = currentPosition
//                    handler.postDelayed(this, 1)
//                }
//            }
//        }
//    }
