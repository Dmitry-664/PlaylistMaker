package com.example.playlistmaker.util

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.data.search.impl.SearchHistoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.player.impl.PlayerRepositoryImpl
import com.example.playlistmaker.data.search.impl.SearchRepositoryImpl
import com.example.playlistmaker.data.settings.SettingsRepositoryImpl
import com.example.playlistmaker.domain.search.api.SearchHistory
import com.example.playlistmaker.domain.search.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.player.api.PlayerInteractor
import com.example.playlistmaker.domain.player.api.PlayerRepository
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.search.api.SearchInteractor
import com.example.playlistmaker.domain.search.api.SearchRepository
import com.example.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.example.playlistmaker.domain.settings.api.SettingsInteractor
import com.example.playlistmaker.domain.settings.api.SettingsRepository
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl

object Creator {

    const val PREF_NAME = "pref_name"
    const val THEME_SHARED_PREFERENCES = "theme_mode"

    private fun getSearchRepository(): SearchRepository {
        return SearchRepositoryImpl(RetrofitNetworkClient())
    }
    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(getSearchRepository())
    }

    private fun getSharedPrefRepository(sharedPref: SharedPreferences): SearchHistory {
        return SearchHistoryImpl(sharedPref)
    }

    fun provideSharedPrefRepositoryInteractor(context: Context): SearchHistoryInteractor {
        val sharedPref = context.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        return SearchHistoryInteractorImpl(getSharedPrefRepository(sharedPref))
    }
    private fun getPlayerRepository(mediaPlayer: MediaPlayer): PlayerRepository {
        return PlayerRepositoryImpl(mediaPlayer)
    }

    fun providePlayerInteractor(mediaPlayer: MediaPlayer): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository(mediaPlayer))
    }

    private fun getSettingsRepository(sharedPrefs: SharedPreferences): SettingsRepository {
        return SettingsRepositoryImpl(sharedPrefs)
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        val sharedPreferences = context.getSharedPreferences(THEME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val settingsRepository = getSettingsRepository(sharedPreferences)
        return SettingsInteractorImpl(settingsRepository)
    }
}