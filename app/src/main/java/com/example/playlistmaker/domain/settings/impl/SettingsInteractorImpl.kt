package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.api.AppTheme
import com.example.playlistmaker.domain.settings.api.SettingsInteractor
import com.example.playlistmaker.domain.settings.api.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository): SettingsInteractor {
    override fun getTheme(): AppTheme = settingsRepository.getTheme()

    override fun instTheme(appTheme: AppTheme) {
        settingsRepository.instTheme(appTheme)
    }

}