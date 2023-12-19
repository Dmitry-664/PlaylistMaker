package com.example.playlistmaker.ui.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.settings.api.SettingsInteractor
import com.example.playlistmaker.util.Creator

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private val switchThemeLD = MutableLiveData(settingsInteractor.getTheme())
    val switchTheme: LiveData<Boolean> = switchThemeLD


    fun themeSwitcher(isChecked: Boolean) {
        switchThemeLD.value = isChecked
        settingsInteractor.instTheme(isChecked)
    }


    companion object {
        fun getViewModelFactory(context: Context): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    val settingsInteractor = Creator.provideSettingsInteractor(context)
                    SettingsViewModel(settingsInteractor)
                }
            }
        }
    }
}