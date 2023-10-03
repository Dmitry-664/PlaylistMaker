package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_QUERY = "SEARCH_QUERY"
        const val PREF_NAME = "pref_name"
        const val ITEMS_TO_UPDATE = 10
    }

    private lateinit var inputEditText: EditText
    private var currentSearchQuery: String = ""
    private lateinit var trackRv: RecyclerView
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private var trackList: MutableList<Track> = mutableListOf()
    private lateinit var adapter: SearchAdapter
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var rvTrackHistory: RecyclerView
    private lateinit var searchHistoryLayout: LinearLayout
    private lateinit var sharedPreferencesHistory: SharedPreferences
    private var searchHistoryList: MutableList<Track> = mutableListOf()
    private lateinit var searchHistory: SearchHistory

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, currentSearchQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputEditText.setText(savedInstanceState.getString(SEARCH_QUERY))
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchHistoryLayout = findViewById(R.id.searchHistoryLayout)
        rvTrackHistory = findViewById(R.id.rvTracksHistory)
        trackRv = findViewById(R.id.rvTracks)
        inputEditText = findViewById(R.id.inputEditText)
        val nothingFoundSearch = findViewById<LinearLayout>(R.id.nothing_found_search_layout)
        val noConnectSearch = findViewById<LinearLayout>(R.id.no_connect_layout)
        val searchRefreshButton = findViewById<Button>(R.id.search_refresh)
        val clearHistoryButton = findViewById<Button>(R.id.clearHistoryButton)
        val backButton = findViewById<Button>(R.id.buttonBack)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        sharedPreferencesHistory = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferencesHistory)
        adapter = SearchAdapter(trackList) { trackList ->
            searchHistory.add(trackList)
        }
        trackRv.adapter = adapter
        searchAdapter = SearchAdapter(searchHistoryList) { searchHistoryList ->
            searchHistory.add(searchHistoryList)
            readHistory()
            searchAdapter.notifyItemRangeChanged(0, ITEMS_TO_UPDATE)
        }
        rvTrackHistory.adapter = searchAdapter
        readHistory()
        historyVisible()

        fun sendSearch() {
            trackRv.visibility = View.VISIBLE
            nothingFoundSearch.visibility = View.GONE
            noConnectSearch.visibility = View.GONE
            searchHistoryLayout.visibility = View.GONE
            if (inputEditText.text?.isNotEmpty() == true) {
                iTunesService.search(inputEditText.text.toString())
                    .enqueue(object : Callback<SearchResponse> {

                        @SuppressLint("NotifyDataSetChanged")
                        override fun onResponse(
                            call: Call<SearchResponse>,
                            response: Response<SearchResponse>
                        ) {
                            if (response.code() == 200) {
                                trackList.clear()
                                if (response.body()?.results?.isNotEmpty() == true) {
                                    trackList.addAll(response.body()?.results!!)
                                } else {
                                    trackList.clear()
                                    nothingFoundSearch.visibility = View.VISIBLE
                                }
                            } else {
                                trackList.clear()
                                noConnectSearch.visibility = View.VISIBLE
                            }
                            adapter.notifyDataSetChanged()
                        }

                        override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                            trackList.clear()
                            noConnectSearch.visibility = View.VISIBLE
                        }
                    })
            }
        }

        backButton.setOnClickListener {
            finish()
        }


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
            trackRv.visibility = View.GONE
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                currentSearchQuery = inputEditText.text.toString()
                searchHistoryLayout.visibility = searchHistoryVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) = Unit
        }
        inputEditText.addTextChangedListener(textWatcher)

        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            readHistory()
            trackRv.visibility = View.GONE
            if (searchHistoryList.isNotEmpty() && hasFocus && inputEditText.text.isNullOrEmpty()) {
                searchHistoryLayout.visibility = View.VISIBLE
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
            searchHistory.clear()
            searchHistoryList.clear()
            searchAdapter.notifyItemRangeChanged(0, searchHistoryList.size)
            searchHistoryLayout.visibility = View.GONE
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
        searchHistoryList.addAll(searchHistory.readListTrack())
        searchAdapter.notifyItemRangeChanged(0, searchHistoryList.size)
        Log.e("myLog", "readHistory + $searchHistoryList")
    }

    private fun historyVisible() {
        if (searchHistoryList.isNotEmpty()) {
            searchHistoryLayout.visibility = View.VISIBLE
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