package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.SearchResponse
import com.example.playlistmaker.data.dto.TrackRequest
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.api.SearchRepository
import com.example.playlistmaker.util.SearchResults

class SearchRepositoryImpl(private val networkClient: NetworkClient): SearchRepository {
    override fun searchTracks(expression: String): SearchResults<List<Track>> {
        val response = networkClient.doRequest(TrackRequest(expression))
        return when (response.resultCode) {
            200 -> {
                SearchResults.Success((response as SearchResponse).results.map {
                    Track(
                        trackName = it.trackName,
                        artistName = it.artistName.orEmpty(),
                        trackTimeMillis = it.trackTimeMillis,
                        artworkUrl100 = it.artworkUrl100.orEmpty(),
                        trackId = it.trackId,
                        collectionName = it.collectionName.orEmpty(),
                        releaseDate = it.releaseDate.orEmpty(),
                        primaryGenreName = it.primaryGenreName,
                        country = it.country,
                        previewUrl = it.previewUrl.orEmpty()
                    )
                })
            }

            -1 -> {
                SearchResults.Error("noConnectSearch")
            }
            else -> {
                SearchResults.Error("nothingFoundSearch")
            }
        }
    }

}