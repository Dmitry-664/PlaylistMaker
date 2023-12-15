package com.example.playlistmaker.ui.search

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.search.ViewState
import com.example.playlistmaker.domain.search.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.SearchInteractor
import com.example.playlistmaker.util.Creator

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private val stateHistoryLD = MutableLiveData(searchHistoryInteractor.readListTrack())
    val stateHistory: LiveData<List<Track>> = stateHistoryLD
    private val stateSearchLD = MutableLiveData<ViewState>()
    val stateSearch: LiveData<ViewState> = stateSearchLD

    private val handler = Handler(Looper.getMainLooper())
    private var latestSearchText: String? = null

    init {
        renderState(ViewState.HistoryLoaded(searchHistoryInteractor.readListTrack()))
    }

    private fun renderState(state: ViewState) {
        stateSearchLD.postValue(state)
    }
    fun sendSearch(query: String) {
        latestSearchText = query
        if (query.isEmpty()) {
            renderState(ViewState.HistoryLoaded(searchHistoryInteractor.readListTrack()))
        } else {
            debounceSearch { performSearch(query) }
        }
    }
    private fun debounceSearch(request: () -> Unit) {
        handler.removeCallbacksAndMessages(TOKEN)
        handler.postDelayed({ request() }, TOKEN, DEBOUNCE_DELAY)
    }
    fun onDestroyHandlerRemove() {
        handler.removeCallbacksAndMessages(TOKEN)
    }
    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(TOKEN)
    }
    fun addTracktoHistoryInvisible(track: Track) {
        searchHistoryInteractor.addTrack(track)
    }
    fun addTrackToHistory(track: Track) {
        searchHistoryInteractor.addTrack(track)
        renderState(ViewState.HistoryLoaded(searchHistoryInteractor.readListTrack()))
    }
    fun clearSearchHistory() {
        searchHistoryInteractor.clear()
        renderState(ViewState.HistoryLoaded(searchHistoryInteractor.readListTrack()))
    }
    private fun performSearch(query: String) {
        renderState(ViewState.Loading)
        if (query.isBlank()) {
            renderState(ViewState.HistoryLoaded(searchHistoryInteractor.readListTrack()))
        } else {
            searchInteractor.searchTracks(query, object : SearchInteractor.TracksConsumer {
                override fun consume(found: List<Track>?, errorId: String?) {
                    handler.post {
                        when {
                            errorId != null -> renderState(ViewState.NoConnect)
                            found.isNullOrEmpty() -> renderState(ViewState.NothingFound)
                            else -> renderState(ViewState.TracksLoaded(found))
                        }
                    }
                }
            })
        }
    }

    companion object {
        private val TOKEN = Any()
        private const val DEBOUNCE_DELAY = 300L

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context = (this[APPLICATION_KEY] as Application).applicationContext
                val searchHistoryInteractor = Creator.provideSharedPrefRepositoryInteractor(context)
                val searchInteractor = Creator.provideSearchInteractor()
                SearchViewModel(searchHistoryInteractor, searchInteractor)
            }
        }
    }

}
