package com.example.playlistmaker.data.settings

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.example.playlistmaker.domain.settings.api.SettingsRepository

const val THEME_PREF_KEY = "pref_key1"

class SettingsRepositoryImpl(private val sharedPrefs: SharedPreferences) : SettingsRepository {
        override fun getTheme(): Boolean = sharedPrefs.getBoolean(THEME_PREF_KEY, false)
//    override fun getTheme(): Boolean {
//        val savedValue = sharedPrefs.getString(THEME_PREF_KEY, "")
//        return if (savedValue != null && savedValue.isNotEmpty()) {
//            savedValue.toBoolean()
//        } else {
//            false
//        }
//    }

    override fun instTheme(darkTheme: Boolean) {
        sharedPrefs.edit { putBoolean(THEME_PREF_KEY, darkTheme) }
    }
}

