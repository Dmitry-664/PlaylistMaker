package com.example.playlistmaker.data.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.settings.api.SharingRepository

class SharingRepositoryImpl(private val context: Context) : SharingRepository {

    override fun shareButton() {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            val shareMessage = context.getString(R.string.praktikum)
            putExtra(Intent.EXTRA_TEXT, shareMessage)
            context.startActivity(Intent(this))
        }
    }

    override fun supportWrite() {
        val topic = context.getString(R.string.support_topic)
        val message = context.getString(R.string.support_text)
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.email)))
        intent.putExtra(Intent.EXTRA_SUBJECT, topic)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        context.startActivity(intent)
    }

    override fun agreementUser() {
        val url = context.getString(R.string.yandex_offer)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    }
}