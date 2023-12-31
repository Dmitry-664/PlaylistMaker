package com.example.playlistmaker.ui.activity

import android.os.Handler
import android.os.Looper

object DebounceActivity {

    private const val CLICK_DEBOUNCE_DELAY = 1000L
    private const val SEARCH_DEBOUNCE_DELAY = 2000L
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    fun clickDebounce(): Boolean {
        val click = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return click
    }

    fun searchDebounce(searchRunnable: Runnable) {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

}