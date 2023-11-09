package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.data.impl.SearchHistoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.impl.TrackRepositoryImpl
import com.example.playlistmaker.domain.api.SearchHistory
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.TrackInteractorImpl

object Creator {

    const val PREF_NAME = "pref_name"
    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    private fun getSharedPrefRepository(sharedPref: SharedPreferences): SearchHistory {
        return SearchHistoryImpl(sharedPref)
    }

    fun provideSharedPrefRepositoryInteractor(context: Context): SearchHistoryInteractor {
        val sharedPref = context.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        return SearchHistoryInteractorImpl(getSharedPrefRepository(sharedPref))
    }
}