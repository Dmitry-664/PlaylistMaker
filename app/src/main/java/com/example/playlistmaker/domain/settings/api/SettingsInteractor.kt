package com.example.playlistmaker.domain.settings.api

interface SettingsInteractor {
    fun getTheme(): AppTheme
    fun instTheme(appTheme: AppTheme)
}