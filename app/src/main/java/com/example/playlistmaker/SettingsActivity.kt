package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker.R

class SettingsActivity : AppCompatActivity() {
    private var isDarkThemeEnabled = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<Button>(R.id.buttonBack)
        backButton.setOnClickListener {
            finish()
        }
        val switchButton = findViewById<SwitchCompat>(R.id.switchButton)
        switchButton.setOnCheckedChangeListener { _, isChecked ->
            isDarkThemeEnabled = isChecked
            applyTheme()
        }
        val shareButton = findViewById<Button>(R.id.buttonShare)
        shareButton.setOnClickListener{
            shareApp()
        }
        val supportWrite = findViewById<Button>(R.id.writeToSupport)
        supportWrite.setOnClickListener{
            supportApp()
        }
        val agreementUser = findViewById<Button>(R.id.user_agreement)
        agreementUser.setOnClickListener{
            agreementApp()
        }
    }

    private fun applyTheme() {
        if (isDarkThemeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
    private fun shareApp() {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        val shareMessage = "Курсы от Яндекс-Практикума: " +
                "https://practicum.yandex.ru/"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent(shareIntent))
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
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
    private fun agreementApp() {
        val url = getString(R.string.yandex_offer)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}