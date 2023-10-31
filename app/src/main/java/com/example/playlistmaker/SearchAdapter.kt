package com.example.playlistmaker

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.DebounceActivity.clickDebounce
import com.google.gson.Gson

const val SOMETHING_KEY_TRACK = "something_key_track"

class SearchAdapter(
    private var trackList: MutableList<Track>,
    private val itemClickListener: (Track) -> Unit
) : RecyclerView.Adapter<SearchViewHolder>() {

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
            Log.e("myLog", "Track push $position")
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    private fun openAudioPlayer(holder: SearchViewHolder, track: Track) {
        if (clickDebounce()) {
            val intent = Intent(holder.itemView.context, AudioPlayerActivity::class.java)
            intent.putExtra(SOMETHING_KEY_TRACK, Gson().toJson(track))
            holder.itemView.context.startActivity(intent)
        }

    }
}