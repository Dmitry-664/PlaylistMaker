package com.example.playlistmaker.screen.screen.elements

import com.example.playlistmaker.R
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.switch.KSwitch
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView

object SettingsScreenElements: KScreen<SettingsScreenElements>() {
    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    val headerSettingsName = KTextView{withId(R.id.settings_name)}
    val darkThemeSettingsName = KTextView{withId(R.id.darkThemeSettingsName)}
    val shareSettingsName = KTextView{withId(R.id.shareSettings)}
    val writeSettingsName = KTextView{withId(R.id.writeSettings)}
    val userAgreementName = KTextView{withId(R.id.userTextView)}
    val switchButton = KSwitch{withId(R.id.switchButton)}
    val buttonShare = KButton{withId(R.id.buttonShare)}
    val writeToSupport = KButton{withId(R.id.writeToSupport)}
    val userAgreement = KButton{ withId(R.id.userAgreement) }
}