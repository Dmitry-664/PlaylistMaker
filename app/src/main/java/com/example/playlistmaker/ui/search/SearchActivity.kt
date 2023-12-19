package com.example.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.search.ViewState
import com.example.playlistmaker.ui.activity.DebounceActivity


class SearchActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_QUERY = "SEARCH_QUERY"
    }

    private lateinit var inputEditText: EditText
    private var currentSearchQuery: String = ""
    private lateinit var trackRv: RecyclerView
    private lateinit var adapter: SearchAdapter
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var rvTrackHistory: RecyclerView
    private lateinit var searchHistoryLayout: LinearLayout
    private var searchHistoryList: MutableList<Track> = mutableListOf()
    private lateinit var nothingFoundSearch: LinearLayout
    private lateinit var noConnectSearch: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var clearButton: ImageView
    private val debounceActivity = DebounceActivity
    private val viewModel: SearchViewModel by viewModels<SearchViewModelImpl> {
        SearchViewModelImpl.getViewModelFactory()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, inputEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val lastSearchQuery = savedInstanceState.getString(SEARCH_QUERY) ?: ""
        viewModel.sendSearch(lastSearchQuery)
        inputEditText.setText(lastSearchQuery)
    }

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchHistoryLayout = findViewById(R.id.searchHistoryLayout)
        rvTrackHistory = findViewById(R.id.rvTracksHistory)
        trackRv = findViewById(R.id.rvTracks)
        inputEditText = findViewById(R.id.inputEditText)
        nothingFoundSearch = findViewById(R.id.nothing_found_search_layout)
        noConnectSearch = findViewById(R.id.no_connect_layout)
        val searchRefreshButton = findViewById<Button>(R.id.search_refresh)
        val clearHistoryButton = findViewById<Button>(R.id.clearHistoryButton)
        val backButton = findViewById<Button>(R.id.buttonBack)
        clearButton = findViewById(R.id.clearIcon)
        progressBar = findViewById(R.id.progressBar)

        adapter = SearchAdapter {
            if (debounceActivity.clickDebounce()) {
                viewModel.addTracktoHistoryInvisible(it)
            }
        }

        searchAdapter = SearchAdapter {
            if (debounceActivity.clickDebounce()) {
                viewModel.addTrackToHistory(it)
            }
        }

        trackRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        trackRv.adapter = adapter

        rvTrackHistory.adapter = searchAdapter

        backButton.setOnClickListener {
            finish()
        }


        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) =
                Unit

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearButton.visibility = clearButtonVisibility(p0)
                currentSearchQuery = inputEditText.text.toString()
                val queryNew = p0?.toString().orEmpty()
                currentSearchQuery = queryNew
                viewModel.sendSearch(queryNew)
            }

            override fun afterTextChanged(p0: Editable?) = Unit
        }
        textWatcher.let {
            inputEditText.addTextChangedListener(it)
        }

        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            trackRv.isVisible = false
            if (searchHistoryList.isNotEmpty() && hasFocus && inputEditText.text.isNullOrEmpty()) {
                searchHistoryLayout.isVisible = true
            } else View.GONE
        }
        searchRefreshButton.setOnClickListener {
            viewModel.sendSearch(currentSearchQuery)
        }

        clearButton.visibility = View.INVISIBLE
        clearButton.setOnClickListener {
            inputEditText.text?.clear()
            closeKeyboard()
            progressBar.isVisible = false
            trackRv.isVisible = false
            nothingFoundSearch.isVisible = false
            noConnectSearch.isVisible = false
            searchHistoryLayout.isVisible = false
            searchAdapter.notifyDataSetChanged()
            inputEditText.clearFocus()
        }

        clearHistoryButton.setOnClickListener {
            viewModel.clearSearchHistory()
            searchAdapter.notifyItemRangeChanged(0, 10)
        }
        viewModel.stateHistory.observe(this) {
            showHistory(it)
        }
        viewModel.stateSearch.observe(this) {
            executeState(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroyHandlerRemove()
    }

    private fun closeKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
    }


    private fun executeState(state: ViewState) {
        when (state) {
            is ViewState.TracksLoaded -> showTracks(state.tracks)
            is ViewState.HistoryLoaded -> showHistory(state.historyTracks)
            is ViewState.NoConnect -> showNoConnection()
            is ViewState.Loading -> showProgressBar()
            is ViewState.NothingFound -> showNothing()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showTracks(tracks: List<Track>?) {
        progressBar.isVisible = false
        trackRv.isVisible = true
        nothingFoundSearch.isVisible = false
        noConnectSearch.isVisible = false
        searchHistoryLayout.isVisible = false
        with (adapter) {
            trackList.clear()
            trackList.addAll(tracks!!)
            notifyItemRangeChanged(0, tracks.size)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showHistory(historyTracks: List<Track>) {
        progressBar.isVisible = false
        trackRv.isVisible = false
        nothingFoundSearch.isVisible = false
        noConnectSearch.isVisible = false
        if (historyTracks.isNotEmpty()) {
            searchHistoryLayout.isVisible = true
            with(searchAdapter) {
                trackList.clear()
                trackList.addAll(historyTracks)
                notifyItemRangeChanged(0, historyTracks.size)
            }
        } else  {
            searchHistoryLayout.isVisible = false
        }
    }

    private fun showNoConnection() {
        progressBar.isVisible = false
        trackRv.isVisible = false
        nothingFoundSearch.isVisible = false
        noConnectSearch.isVisible = true
        searchHistoryLayout.isVisible = false
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showProgressBar() {
        progressBar.isVisible = true
        trackRv.isVisible = false
        nothingFoundSearch.isVisible = false
        noConnectSearch.isVisible = false
        searchHistoryLayout.isVisible = false
        adapter.trackList.clear()
        adapter.notifyDataSetChanged()
    }

    private fun showNothing() {
        progressBar.isVisible = false
        trackRv.isVisible = false
        nothingFoundSearch.isVisible = true
        noConnectSearch.isVisible = false
        searchHistoryLayout.isVisible = false
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}