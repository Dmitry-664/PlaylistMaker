package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun readListTrack(): List<Track>
    fun addTrack(newTrack: Track)
    fun clear()
}