package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.presentation.activity.KEY_STATUS_SHARED_PREFERENCES
import com.example.playlistmaker.presentation.activity.THEME_SHARED_PREFERENCES

class ThemeModeNight : Application() {
    var darkTheme: Boolean = false
    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(THEME_SHARED_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(KEY_STATUS_SHARED_PREFERENCES, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}