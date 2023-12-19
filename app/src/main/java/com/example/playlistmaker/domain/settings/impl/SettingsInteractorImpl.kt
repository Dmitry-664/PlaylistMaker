package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.api.SettingsInteractor
import com.example.playlistmaker.domain.settings.api.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository): SettingsInteractor {
    override fun getTheme(): Boolean = settingsRepository.getTheme()

    override fun instTheme(darkTheme: Boolean) {
        settingsRepository.instTheme(darkTheme)
    }

}