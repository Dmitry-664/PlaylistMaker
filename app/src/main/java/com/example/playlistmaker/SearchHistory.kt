package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

const val SHARED_PREF_KEY = "pref_key"
const val SIZE_TRACK = 10
class SearchHistory(sharedPreferences: SharedPreferences) {
    private val sharedPrefs = sharedPreferences

    fun readListTrack(): List<Track> {
        val json = sharedPrefs.getString(SHARED_PREF_KEY, null) ?: return emptyList()
        return Gson().fromJson(json, Array<Track>::class.java).toList()
    }
    fun clear() = sharedPrefs.edit().clear().apply()

    fun add(newTrack: Track){
        var sizeList = readListTrack().toMutableList()
        sizeList.removeIf { it.trackId == newTrack.trackId }
        sizeList.add(0, newTrack)
        if (sizeList.size > SIZE_TRACK) {
            sizeList = sizeList.subList(0, SIZE_TRACK)
        }
        val json = Gson().toJson(sizeList)
        sharedPrefs.edit()
            .putString(SHARED_PREF_KEY, json)
            .apply()
    }
}