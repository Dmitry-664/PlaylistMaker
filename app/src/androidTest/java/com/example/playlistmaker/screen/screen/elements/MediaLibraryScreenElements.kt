package com.example.playlistmaker.screen.screen.elements

import com.example.playlistmaker.R
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.text.KTextView

object MediaLibraryScreenElements : KScreen<MediaLibraryScreenElements>() {
    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    val textMediaName = KTextView {withId(R.id.mediaName)}

}