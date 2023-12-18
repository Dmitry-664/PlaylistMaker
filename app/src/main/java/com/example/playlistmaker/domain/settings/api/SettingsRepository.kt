package com.example.playlistmaker.domain.settings.api

interface SettingsRepository {
    fun getTheme(): Boolean           //  извлекает
    fun instTheme(darkTheme: Boolean)  //  устанавливает
}