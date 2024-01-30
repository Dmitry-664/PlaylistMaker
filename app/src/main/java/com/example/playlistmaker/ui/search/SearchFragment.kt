package com.example.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.search.ViewState
import com.example.playlistmaker.ui.activity.DebounceActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    companion object {
        private const val SEARCH_QUERY = "SEARCH_QUERY"
    }

    private var searchHistoryList: MutableList<Track> = mutableListOf()
    private lateinit var nothingFoundSearch: LinearLayout
    private lateinit var noConnectSearch: LinearLayout
    private var currentSearchQuery: String = ""
    private lateinit var adapter: SearchAdapter
    private lateinit var searchAdapter: SearchAdapter
    private val debounceActivity = DebounceActivity
    private val viewModel by viewModel<SearchViewModelImpl>()




    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nothingFoundSearch = binding.nothingFoundSearchLayout
        noConnectSearch = binding.noConnectLayout
        val searchRefreshButton = binding.searchRefresh
        val clearHistoryButton = binding.clearHistoryButton


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

        binding.rvTracks.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvTracks.adapter = adapter

        binding.rvTracksHistory.adapter = searchAdapter



        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) =
                Unit

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(p0)
                currentSearchQuery = binding.inputEditText.text.toString()
                val queryNew = p0?.toString().orEmpty()
                currentSearchQuery = queryNew
                viewModel.sendSearch(queryNew)
            }

            override fun afterTextChanged(p0: Editable?) = Unit
        }
        textWatcher.let {
            binding.inputEditText.addTextChangedListener(it)
        }

        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            binding.rvTracks.isVisible = false
            if (searchHistoryList.isNotEmpty() && hasFocus && binding.inputEditText.text.isNullOrEmpty()) {
                binding.searchHistoryLayout.isVisible = true
            } else View.GONE
        }
        searchRefreshButton.setOnClickListener {
            viewModel.sendSearch(currentSearchQuery)
        }

        binding.clearIcon.visibility = View.INVISIBLE
        binding.clearIcon.setOnClickListener {
            binding.inputEditText.text?.clear()
            closeKeyboard()
            binding.progressBar.isVisible = false
            binding.rvTracks.isVisible = false
            nothingFoundSearch.isVisible = false
            noConnectSearch.isVisible = false
            binding.searchHistoryLayout.isVisible = false
            searchAdapter.notifyDataSetChanged()
            binding.inputEditText.clearFocus()
        }

        clearHistoryButton.setOnClickListener {
            viewModel.clearSearchHistory()
            searchAdapter.notifyItemRangeChanged(0, 10)
        }
        viewModel.stateHistory.observe(viewLifecycleOwner) {
            showHistory(it)
        }
        viewModel.stateSearch.observe(viewLifecycleOwner) {
            executeState(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroyHandlerRemove()
    }

    private fun closeKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.inputEditText.windowToken, 0)
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
        binding.progressBar.isVisible = false
        binding.rvTracks.isVisible = true
        nothingFoundSearch.isVisible = false
        noConnectSearch.isVisible = false
        binding.searchHistoryLayout.isVisible = false
        with (adapter) {
            trackList.clear()
            trackList.addAll(tracks!!)
            notifyItemRangeChanged(0, tracks.size)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showHistory(historyTracks: List<Track>) {
        binding.progressBar.isVisible = false
        binding.rvTracks.isVisible = false
        nothingFoundSearch.isVisible = false
        noConnectSearch.isVisible = false
        if (historyTracks.isNotEmpty()) {
            binding.searchHistoryLayout.isVisible = true
            with(searchAdapter) {
                trackList.clear()
                trackList.addAll(historyTracks)
                notifyItemRangeChanged(0, historyTracks.size)
            }
        } else  {
            binding.searchHistoryLayout.isVisible = false
        }
    }

    private fun showNoConnection() {
        binding.progressBar.isVisible = false
        binding.rvTracks.isVisible = false
        nothingFoundSearch.isVisible = false
        noConnectSearch.isVisible = true
        binding.searchHistoryLayout.isVisible = false
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showProgressBar() {
        binding.progressBar.isVisible = true
        binding.rvTracks.isVisible = false
        nothingFoundSearch.isVisible = false
        noConnectSearch.isVisible = false
        binding.searchHistoryLayout.isVisible = false
        adapter.trackList.clear()
        adapter.notifyDataSetChanged()
    }

    private fun showNothing() {
        binding.progressBar.isVisible = false
        binding.rvTracks.isVisible = false
        nothingFoundSearch.isVisible = true
        noConnectSearch.isVisible = false
        binding.searchHistoryLayout.isVisible = false
    }
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}