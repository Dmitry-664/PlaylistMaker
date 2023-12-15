package com.example.playlistmaker.ui.settings


import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.ThemeModeNight


class SettingsActivity : AppCompatActivity() {


    private val viewModel by viewModels<SettingsViewModel> {
        SettingsViewModel.getViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val backButton = findViewById<Button>(R.id.buttonBack)
        backButton.setOnClickListener {
            finish()
        }
        val switcher = findViewById<SwitchCompat>(R.id.switchButton)
        viewModel.switchTheme.observe(this) { isChecked ->
            switcher.isChecked = isChecked
        }

        switcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.themeSwitcher(isChecked)
        }

        val shareButton = findViewById<Button>(R.id.buttonShare)
        shareButton.setOnClickListener {
            viewModel.shareButton()
        }
        val supportWrite = findViewById<Button>(R.id.writeToSupport)
        supportWrite.setOnClickListener {
            viewModel.supportWrite()
        }
        val agreementUser = findViewById<Button>(R.id.user_agreement)
        agreementUser.setOnClickListener {
            viewModel.agreementUser()
        }
    }
}
//        val isDeviceDarkMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
//        ThemeModeNight.switchTheme(isDeviceDarkMode)

//        themeSwitcher.isChecked = (applicationContext as ThemeModeNight).darkTheme
//            (applicationContext as ThemeModeNight).switchTheme(isChecked)
//            val sharedPrefs = getSharedPreferences(THEME_SHARED_PREFERENCES, MODE_PRIVATE)
//            sharedPrefs.edit()
//                .putBoolean(KEY_STATUS_SHARED_PREFERENCES, isChecked)
//                .apply()
//
