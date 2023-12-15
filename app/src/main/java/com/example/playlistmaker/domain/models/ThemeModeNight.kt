package com.example.playlistmaker.domain.models

import androidx.appcompat.app.AppCompatDelegate


object ThemeModeNight {
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