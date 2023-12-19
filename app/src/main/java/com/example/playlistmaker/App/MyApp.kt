package com.example.playlistmaker.App

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.util.Creator

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val interactor = Creator.provideSettingsInteractor(this)

        val darkTheme = interactor.getTheme()
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

