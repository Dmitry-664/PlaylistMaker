package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_QUERY = "SEARCH_QUERY"
    }

    private lateinit var inputEditText: EditText
    private var currentSearchQuery: String = ""
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private val trackList = ArrayList<Track>()
    private val adapter = SearchAdapter()
    private lateinit var trackRv: RecyclerView
    private val nothingFoundSearch by lazy { findViewById<LinearLayout>(R.id.nothing_found_search_layout) }
    private val noConnectSearch by lazy { findViewById<LinearLayout>(R.id.no_connect_layout) }
    private val searchRefreshButton by lazy { findViewById<Button>(R.id.search_refresh) }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, currentSearchQuery)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        if (savedInstanceState != null) {
            currentSearchQuery = savedInstanceState.getString(SEARCH_QUERY, "")
            inputEditText.setText(currentSearchQuery)
        }

        trackRv = findViewById(R.id.rvTracks)
        adapter.trackList = trackList

        fun sendSearch() {
            trackRv.visibility = View.VISIBLE
            nothingFoundSearch.visibility = View.GONE
            noConnectSearch.visibility = View.GONE
            if (inputEditText.text?.isNotEmpty() == true) {
                iTunesService.search(inputEditText.text.toString())
                    .enqueue(object : Callback<SearchResponse> {

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


        val backButton = findViewById<Button>(R.id.buttonBack)
        backButton.setOnClickListener {
            finish()
        }

        inputEditText = findViewById(R.id.inputEditText)
        inputEditText.setOnClickListener {
        }

        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        clearButton.visibility = View.INVISIBLE
        clearButton.setOnClickListener {
            inputEditText.text?.clear()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            inputEditText.clearFocus()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                currentSearchQuery = inputEditText.text.toString()
            }

            override fun afterTextChanged(s: Editable?) = Unit
        }
        inputEditText.addTextChangedListener(textWatcher)

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendSearch()
            }
            false
        }
        searchRefreshButton.setOnClickListener {
            sendSearch()
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
    }

    data class SearchResponse(
        val resultCount: Int,
        val results: List<Track>
    )

}