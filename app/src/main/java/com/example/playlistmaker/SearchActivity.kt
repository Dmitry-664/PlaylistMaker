package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.models.Track

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_QUERY = "SEARCH_QUERY"
        const val ITEMS_TO_UPDATE = 10
    }

    private lateinit var inputEditText: EditText
    private var currentSearchQuery: String = ""
    private lateinit var trackRv: RecyclerView
    private val trackInteractor = Creator.provideTrackInteractor()
    private var trackList: MutableList<Track> = mutableListOf()
    private lateinit var adapter: SearchAdapter
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var rvTrackHistory: RecyclerView
    private lateinit var searchHistoryLayout: LinearLayout
    private lateinit var searchHistoryInteractor: SearchHistoryInteractor
    private var searchHistoryList: MutableList<Track> = mutableListOf()
    private lateinit var nothingFoundSearch: LinearLayout
    private lateinit var noConnectSearch: LinearLayout


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, currentSearchQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputEditText.setText(savedInstanceState.getString(SEARCH_QUERY))
    }

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchHistoryInteractor = Creator.provideSharedPrefRepositoryInteractor(applicationContext)
        searchHistoryLayout = findViewById(R.id.searchHistoryLayout)
        rvTrackHistory = findViewById(R.id.rvTracksHistory)
        trackRv = findViewById(R.id.rvTracks)
        inputEditText = findViewById(R.id.inputEditText)
        nothingFoundSearch = findViewById(R.id.nothing_found_search_layout)
        noConnectSearch = findViewById(R.id.no_connect_layout)
        val searchRefreshButton = findViewById<Button>(R.id.search_refresh)
        val clearHistoryButton = findViewById<Button>(R.id.clearHistoryButton)
        val backButton = findViewById<Button>(R.id.buttonBack)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
//        sharedPreferencesHistory = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
//        searchHistory = SearchHistory(sharedPreferencesHistory)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        adapter = SearchAdapter(trackList) { track ->
            searchHistoryInteractor.addTrack(track)
        }
        trackRv.adapter = adapter
        searchAdapter = SearchAdapter(searchHistoryList) { searchTrack ->
            searchHistoryInteractor.addTrack(searchTrack)
            readHistory()
            searchAdapter.notifyItemRangeChanged(0, ITEMS_TO_UPDATE)
        }
        rvTrackHistory.adapter = searchAdapter
        readHistory()
        historyVisible()

        backButton.setOnClickListener {
            finish()
        }

        fun sendSearch() {
            if (inputEditText.text?.isNotEmpty() == true) {
                progressBar.isVisible = true
                trackRv.isVisible = false
                nothingFoundSearch.isVisible = false
                noConnectSearch.isVisible = false
                searchHistoryLayout.isVisible = false
                trackInteractor.searchTracks(inputEditText.text.toString(), object : TrackInteractor.TracksConsumer{
                    @SuppressLint("NotifyDataSetChanged")
                    override fun consume(foundTracks: List<Track>) {
                        try{
                            progressBar.isVisible = false
                            trackRv.isVisible = true
                            trackList.clear()
                            if (foundTracks.isNotEmpty()) {
                                trackList.addAll(foundTracks)
                            } else {
                                nothingFoundSearch.isVisible = true
                            }
                            adapter.notifyDataSetChanged()
                        } catch (exception: Exception) {
                            progressBar.isVisible = false
                            trackList.clear()
                            noConnectSearch.isVisible = true
                        }

                    }

                })
            }
            //val searchRunnable = Runnable { sendSearch() }
        }
        val searchRunnable = Runnable { sendSearch() }

        clearButton.visibility = View.INVISIBLE
        clearButton.setOnClickListener {
            inputEditText.text?.clear()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            Log.e("myLog", "Clear button + $searchHistoryList")
            searchAdapter.notifyDataSetChanged()
            inputEditText.clearFocus()
            historyVisible()
            trackRv.isVisible = false
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                currentSearchQuery = inputEditText.text.toString()
                searchHistoryLayout.visibility = searchHistoryVisibility(s)
                DebounceActivity.searchDebounce(searchRunnable)
            }

            override fun afterTextChanged(s: Editable?) = Unit
        }
        inputEditText.addTextChangedListener(textWatcher)

        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            readHistory()
            trackRv.isVisible = false
            if (searchHistoryList.isNotEmpty() && hasFocus && inputEditText.text.isNullOrEmpty()) {
                searchHistoryLayout.isVisible = true
            } else View.GONE
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText.text.isNotEmpty()) {
                    sendSearch()
                } else {
                    readHistory()
                }
            }
            false
        }
        searchRefreshButton.setOnClickListener {
            sendSearch()
        }

        clearHistoryButton.setOnClickListener {
            searchHistoryInteractor.clear()
            searchHistoryList.clear()
            searchAdapter.notifyItemRangeChanged(0, searchHistoryList.size)
            searchHistoryLayout.isVisible = false
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun readHistory() {
        searchHistoryList.clear()
        searchHistoryList.addAll(searchHistoryInteractor.readListTrack())
        searchAdapter.notifyItemRangeChanged(0, searchHistoryList.size)
        Log.e("myLog", "readHistory + $searchHistoryList")
    }

    private fun historyVisible() {
        if (searchHistoryList.isNotEmpty()) {
            searchHistoryLayout.isVisible = true
        } else View.GONE
    }

    fun searchHistoryVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty() && searchHistoryList.isNotEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}