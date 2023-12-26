package com.example.playlistmaker.data.search.impl

import android.content.SharedPreferences
import com.example.playlistmaker.domain.search.api.SearchHistory
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson


const val SHARED_PREF_KEY = "pref_key"
const val SIZE_TRACK = 10
class SearchHistoryImpl(private val sharedPrefs: SharedPreferences, private val gson: Gson): SearchHistory {
    override fun readListTrack(): List<Track> {
        val json = sharedPrefs.getString(SHARED_PREF_KEY, null) ?: return emptyList()
        return gson.fromJson(json, Array<Track>::class.java).toList()
    }
    override fun clear() = sharedPrefs.edit().clear().apply()

    override fun addTrack(newTrack: Track){
        var sizeList = readListTrack().toMutableList()
        sizeList.removeIf { it.trackId == newTrack.trackId }
        sizeList.add(0, newTrack)
        if (sizeList.size > SIZE_TRACK) {
            sizeList = sizeList.subList(0, SIZE_TRACK)
        }
        val json = gson.toJson(sizeList)
        sharedPrefs.edit()
            .putString(SHARED_PREF_KEY, json)
            .apply()
    }
}
