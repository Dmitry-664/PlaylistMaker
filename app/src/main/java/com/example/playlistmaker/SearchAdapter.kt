package com.example.playlistmaker

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class SearchAdapter(
            private var trackList: MutableList<Track>,
            private val itemClickListener: (Track) -> Unit
) : RecyclerView.Adapter<SearchViewHolder>() {
//    fun setTracks(newTracks: MutableList<Track>) {
//        val diffResult = DiffUtil.calculateDiff(TrackDiffCallback(trackList, newTracks))
//        trackList.clear()
//        trackList.addAll(newTracks)
//        diffResult.dispatchUpdatesTo(this)
//    }

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
            Log.e("myLog", "Track push $position")
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
//    private class TrackDiffCallback : DiffUtil.ItemCallback<Track>() {
//
////        override fun getOldListSize(): Int {
////            return oldList.size
////        }
////
////        override fun getNewListSize(): Int {
////            return newList.size
////        }
//
//        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
//            return oldItem.trackId == newItem.trackId
//        }
//
//        override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
//            return oldItem == newItem
//        }
//    }
}