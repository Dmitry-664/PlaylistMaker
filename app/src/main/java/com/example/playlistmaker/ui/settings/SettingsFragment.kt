package com.example.playlistmaker.ui.settings


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.App.MyApp
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchButton.isChecked = viewModel.switchTheme.value ?: false
        viewModel.switchTheme.observe(viewLifecycleOwner) { darkTheme ->
            binding.switchButton.isChecked = darkTheme
            (binding.root.context.applicationContext as? MyApp)?.switchTheme(darkTheme)
        }

        binding.switchButton.setOnCheckedChangeListener { _, isChecked ->
            viewModel.themeSwitcher(isChecked)
        }

        binding.buttonShare.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                val shareMessage = getString(R.string.praktikum)
                putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent(this))
            }
        }

        binding.writeToSupport.setOnClickListener {
            val topic = getString(R.string.support_topic)
            val message = getString(R.string.support_text)
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email)))
            intent.putExtra(Intent.EXTRA_SUBJECT, topic)
            intent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(intent)
        }

        binding.userAgreement.setOnClickListener {
            val url = getString(R.string.yandex_offer)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }
}


