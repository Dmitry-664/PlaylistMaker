package com.example.playlistmaker.domain.settings.api

interface SettingsInteractor {
    fun getTheme(): Boolean
    fun instTheme(darkTheme: Boolean)
}