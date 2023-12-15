package com.example.playlistmaker.domain.models.search

import com.example.playlistmaker.domain.models.Track

sealed interface ViewState {
    object Loading : ViewState
    data class TracksLoaded(val tracks: List<Track>) : ViewState
    data class HistoryLoaded(val historyTracks: List<Track>) : ViewState
    object NothingFound : ViewState
    object NoConnect : ViewState
}