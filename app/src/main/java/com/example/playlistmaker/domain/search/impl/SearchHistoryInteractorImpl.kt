package com.example.playlistmaker.domain.search.impl

import android.util.Log
import com.example.playlistmaker.domain.search.api.SearchHistory
import com.example.playlistmaker.domain.search.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.models.Track

class SearchHistoryInteractorImpl(private val searchHistory: SearchHistory):
    SearchHistoryInteractor {
    override fun readListTrack(): List<Track> {
       return searchHistory.readListTrack().also { Log.d("fff", it.size.toString()) }
    }

    override fun addTrack(newTrack: Track) {
        searchHistory.addTrack(newTrack)
    }

    override fun clear() {
        searchHistory.clear()
    }
}