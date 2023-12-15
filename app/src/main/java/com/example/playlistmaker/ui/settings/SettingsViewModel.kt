package com.example.playlistmaker.ui.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.models.ThemeModeNight
import com.example.playlistmaker.domain.settings.api.AppTheme
import com.example.playlistmaker.domain.settings.api.SettingsInteractor
import com.example.playlistmaker.domain.settings.api.SharingInteractor
import com.example.playlistmaker.util.Creator

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val switchThemeLD = MutableLiveData<Boolean>()
    val switchTheme: LiveData<Boolean> = switchThemeLD


    fun themeSwitcher(isChecked: Boolean) {
        switchThemeLD.value = isChecked
        settingsInteractor.instTheme(AppTheme(isChecked))
        ThemeModeNight.switchTheme(isChecked)
    }

    fun shareButton() {
        sharingInteractor.shareButton()
    }

    fun supportWrite() {
        sharingInteractor.supportWrite()
    }

    fun agreementUser() {
        sharingInteractor.agreementUser()
    }

    companion object {
        fun getViewModelFactory(context: Context): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    val settingsInteractor = Creator.provideSettingsInteractor(context)
                    val sharingInteractor = Creator.provideSharingInteractor(context)
                    SettingsViewModel(settingsInteractor, sharingInteractor)
                }
            }
        }
    }
}