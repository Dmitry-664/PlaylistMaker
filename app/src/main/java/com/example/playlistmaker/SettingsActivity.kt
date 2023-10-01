package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.app.AppCompatDelegate


const val THEME_SHARED_PREFERENCES = "theme_mode"
const val KEY_STATUS_SHARED_PREFERENCES = "status_shared_preferences"

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<Button>(R.id.buttonBack)
        backButton.setOnClickListener {
            finish()
        }

        val themeSwitcher = findViewById<SwitchCompat>(R.id.switchButton)
        themeSwitcher.isChecked = (applicationContext as ThemeModeNight).darkTheme
        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            (applicationContext as ThemeModeNight).switchTheme(isChecked)
            val sharedPrefs = getSharedPreferences(THEME_SHARED_PREFERENCES, MODE_PRIVATE)
            sharedPrefs.edit()
                .putBoolean(KEY_STATUS_SHARED_PREFERENCES, isChecked)
                .apply()
        }
        val shareButton = findViewById<Button>(R.id.buttonShare)
        shareButton.setOnClickListener {
            shareApp()
        }
        val supportWrite = findViewById<Button>(R.id.writeToSupport)
        supportWrite.setOnClickListener {
            supportApp()
        }
        val agreementUser = findViewById<Button>(R.id.user_agreement)
        agreementUser.setOnClickListener {
            agreementApp()
        }
    }


    private fun shareApp() {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            val shareMessage = getString(R.string.praktikum)
            putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent(this))
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun supportApp() {
        val topic = getString(R.string.support_topic)
        val message = getString(R.string.support_text)
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email)))
        intent.putExtra(Intent.EXTRA_SUBJECT, topic)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(intent)
    }

    private fun agreementApp() {
        val url = getString(R.string.yandex_offer)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}