package com.example.playlistmaker.data.settings

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.domain.settings.api.AppTheme
import com.example.playlistmaker.domain.settings.api.SettingsRepository

const val THEME_PREF_KEY = "pref_key"
class SettingsRepositoryImpl(private val sharedPrefs: SharedPreferences): SettingsRepository {
    override fun getTheme(): AppTheme = AppTheme(sharedPrefs.getBoolean(THEME_PREF_KEY, false))

    override fun instTheme(appTheme: AppTheme) {
        sharedPrefs.edit { putBoolean(THEME_PREF_KEY, appTheme.darkTheme) }
    }
}