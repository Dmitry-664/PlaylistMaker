package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackIconMusic = itemView.findViewById<ImageView>(R.id.icon_music)
    private val trackName = itemView.findViewById<TextView>(R.id.text_trackName)
    private val artistName = itemView.findViewById<TextView>(R.id.text_artistName)
    private val trackLenght = itemView.findViewById<TextView>(R.id.track_lenght)

    fun bind(item: Track) {
        trackName.text = item.trackName
        artistName.text = item.artistName
        trackLenght.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)

        Glide.with(itemView.context)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(5))
            .fitCenter()
            .into(trackIconMusic)
    }
}