package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.api.SearchInteractor
import com.example.playlistmaker.domain.search.api.SearchRepository
import com.example.playlistmaker.util.SearchResults
import java.util.concurrent.Executors

class SearchInteractorImpl(private val searchRepository: SearchRepository): SearchInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(expression: String, consumer: SearchInteractor.TracksConsumer) {
        executor.execute {
            when (val resourse = searchRepository.searchTracks(expression)) {
                is SearchResults.Success -> { consumer.consume(resourse.data, null) }
                is SearchResults.Error -> { consumer.consume(null, resourse.message) }
            }
        }
    }
}