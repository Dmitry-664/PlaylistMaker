package com.example.playlistmaker.ui.settings


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker.App.MyApp
import com.example.playlistmaker.R
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {

    private val viewModel by viewModel<SettingsViewModel>()

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
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                val shareMessage = getString(R.string.praktikum)
                putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent(this))
            }
        }
        val supportWrite = findViewById<Button>(R.id.writeToSupport)
        supportWrite.setOnClickListener {
            val topic = getString(R.string.support_topic)
            val message = getString(R.string.support_text)
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email)))
            intent.putExtra(Intent.EXTRA_SUBJECT, topic)
            intent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(intent)
        }
        val agreementUser = findViewById<Button>(R.id.user_agreement)
        agreementUser.setOnClickListener {
            val url = getString(R.string.yandex_offer)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }
}

