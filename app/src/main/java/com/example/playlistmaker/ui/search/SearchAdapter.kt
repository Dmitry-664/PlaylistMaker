package com.example.playlistmaker.ui.search

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.ui.activity.DebounceActivity.clickDebounce
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.player.AudioPlayerActivity
import com.google.gson.Gson

const val SOMETHING_KEY_TRACK = "something_key_track"

class SearchAdapter(private var itemClickListener: ((Track) -> Unit)) : RecyclerView.Adapter<SearchViewHolder>() {
    var trackList: ArrayList<Track> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_layout, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.setIsRecyclable(true)
        holder.itemView.setOnClickListener {
            itemClickListener.invoke(trackList[position])
            openAudioPlayer(holder, trackList[position])
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    private fun openAudioPlayer(holder: SearchViewHolder, track: Track) {
            val intent = Intent(holder.itemView.context, AudioPlayerActivity::class.java)
            intent.putExtra(SOMETHING_KEY_TRACK, Gson().toJson(track))
            holder.itemView.context.startActivity(intent)
            this.notifyItemRangeChanged(0, trackList.size)
    }

}