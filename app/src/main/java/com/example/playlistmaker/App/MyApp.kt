package com.example.playlistmaker.App

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.example.playlistmaker.data.settings.SettingsRepositoryImpl
import com.example.playlistmaker.domain.settings.api.AppTheme
import com.example.playlistmaker.domain.settings.api.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.util.Creator

class MyApp : Application() {
    private var switchOn: Boolean = false
    override fun onCreate() {
        super.onCreate()
        val settingsInTouch = createSettingsInteractor()
        val isDeviceDarkMode =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        ThemeModeNight.switchTheme(isDeviceDarkMode)
        if (isDeviceDarkMode) {
            switchOn = true
            settingsInTouch.instTheme(AppTheme(true))
        } else {
            switchOn = settingsInTouch.getTheme().darkTheme
        }
        ThemeModeNight.switchTheme(switchOn)
    }
    private fun createSettingsInteractor(): SettingsInteractor {
        val sharedPreferences = getSharedPreferences(
            Creator.THEME_SHARED_PREFERENCES, Context.MODE_PRIVATE
        )
        return SettingsInteractorImpl(SettingsRepositoryImpl(sharedPreferences))
    }
}