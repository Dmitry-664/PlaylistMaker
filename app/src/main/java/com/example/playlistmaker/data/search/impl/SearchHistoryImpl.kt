package com.example.playlistmaker.data.search.impl

import android.content.SharedPreferences
import com.example.playlistmaker.domain.search.api.SearchHistory
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson


const val SHARED_PREF_KEY = "pref_key"
const val SIZE_TRACK = 10
class SearchHistoryImpl(private val sharedPrefs: SharedPreferences): SearchHistory {
    override fun readListTrack(): List<Track> {
        val json = sharedPrefs.getString(SHARED_PREF_KEY, null) ?: return emptyList()
        return Gson().fromJson(json, Array<Track>::class.java).toList()
    }
    override fun clear() = sharedPrefs.edit().clear().apply()

    override fun addTrack(newTrack: Track){
        val json = readListTrack()
            .filter { it.trackId == newTrack.trackId }
            .toMutableList()
            .apply { add(0, newTrack) }
            .take(SIZE_TRACK)
            .let(Gson()::toJson)
        sharedPrefs.edit()
            .putString(SHARED_PREF_KEY, json)
            .apply()
    }
}
