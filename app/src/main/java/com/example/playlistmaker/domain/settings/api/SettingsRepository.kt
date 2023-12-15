package com.example.playlistmaker.domain.settings.api

interface SettingsRepository {
    fun getTheme(): AppTheme           //  извлекает
    fun instTheme(appTheme: AppTheme)  //  устанавливает
}