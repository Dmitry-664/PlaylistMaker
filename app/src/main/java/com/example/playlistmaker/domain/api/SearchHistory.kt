package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistory {
    fun readListTrack(): List<Track>
    fun addTrack(newTrack: Track)
    fun clear()
}