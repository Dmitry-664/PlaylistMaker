package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.models.Track


interface SearchInteractor {
    fun searchTracks (expression: String, consumer: TracksConsumer)
    interface TracksConsumer {
        fun consume(found: List<Track>?, errorId: String?)
    }
}