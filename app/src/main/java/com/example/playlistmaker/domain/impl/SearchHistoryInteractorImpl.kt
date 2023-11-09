package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SearchHistory
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.models.Track

class SearchHistoryInteractorImpl(private val searchHistory: SearchHistory): SearchHistoryInteractor{
    override fun readListTrack(): List<Track> {
       return searchHistory.readListTrack()
    }

    override fun addTrack(newTrack: Track) {
        searchHistory.addTrack(newTrack)
    }

    override fun clear() {
        searchHistory.clear()
    }
}