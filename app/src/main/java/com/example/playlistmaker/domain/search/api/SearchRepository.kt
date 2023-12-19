package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.util.SearchResults

interface SearchRepository {
    fun searchTracks (expression: String): SearchResults<List<Track>>
}