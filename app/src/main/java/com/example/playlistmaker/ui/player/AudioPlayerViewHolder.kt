package com.example.playlistmaker.ui.player

import android.icu.text.SimpleDateFormat
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


const val TIME_SONG_PLAYER = "00:00"
class AudioPlayerViewHolder(private val audioPlayerViewHolder: AudioPlayerActivity) {

    private val trackImage = audioPlayerViewHolder.findViewById<ImageView>(R.id.imageAudioPlayer)
    private val nameSong = audioPlayerViewHolder.findViewById<TextView>(R.id.nameSongAudioPlayer)
    private val nameGroup = audioPlayerViewHolder.findViewById<TextView>(R.id.nameGroupAudioPlayer)
    private val timeSong = audioPlayerViewHolder.findViewById<TextView>(R.id.timeSongAudioPlayer)
    private val trackTimes = audioPlayerViewHolder.findViewById<TextView>(R.id.trackTimesAudioInfo)
    private val albumName = audioPlayerViewHolder.findViewById<TextView>(R.id.albumNameAudioInfo)
    private val trackYear = audioPlayerViewHolder.findViewById<TextView>(R.id.trackYearAudioInfo)
    private val trackGenre = audioPlayerViewHolder.findViewById<TextView>(R.id.trackGenreAudioInfo)
    private val trackCountry = audioPlayerViewHolder.findViewById<TextView>(R.id.trackCountryAudioInfo)

    fun bind (item: Track) {
        nameSong.text = item.trackName
        nameGroup.text = item.artistName
        albumName.text = item.collectionName
        trackGenre.text = item.primaryGenreName
        trackCountry.text =
            when(item.country) {
                "USA" -> "Соединённые штаты америки"
                "GBR" -> "Объединенные королевства"
                "CAN" -> "Канада"
                else -> item.country
            }
        timeSong.text = TIME_SONG_PLAYER
        trackTimes.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)
        trackYear.text = LocalDateTime.parse(
            item.releaseDate,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        ).year.toString()


        Glide.with(audioPlayerViewHolder)
            .load(item.artworkUrl100.toString()?.replaceAfterLast('/', "512x512bb.jpg") ?: "")
            .placeholder(R.drawable.ic_placeholder_player)
            .fitCenter()
            .transform(RoundedCorners(15))
            .into(trackImage)

    }
}