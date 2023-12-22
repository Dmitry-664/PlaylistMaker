package com.example.playlistmaker.ui.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.api.SettingsInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private val switchThemeLD = MutableLiveData(settingsInteractor.getTheme())
    val switchTheme: LiveData<Boolean> = switchThemeLD


    fun themeSwitcher(isChecked: Boolean) {
        switchThemeLD.value = isChecked
        settingsInteractor.instTheme(isChecked)
    }

}