package com.example.playlistmaker.ui.settings


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker.App.MyApp
import com.example.playlistmaker.R


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
        switcher.isChecked = viewModel.switchTheme.value ?: false
        viewModel.switchTheme.observe(this) { darkTheme ->
            switcher.isChecked = darkTheme
            (application as? MyApp)?.switchTheme(darkTheme)
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

