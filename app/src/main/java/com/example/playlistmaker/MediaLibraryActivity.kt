package com.example.playlistmaker

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import java.util.Locale


class MediaLibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_library)
    }
}

