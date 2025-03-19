package com.example.playlistmaker.screen.screen.elements


import com.example.playlistmaker.R
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.bottomnav.KBottomNavigationView

object MainScreen : KScreen<MainScreen>() {
    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    val bottomNavigation = KBottomNavigationView{withId(R.id.bottomNavigationView)}

    fun selectTab(id: Int) {
        bottomNavigation {
            setSelectedItem(id)
        }
    }

    fun getTabIdByTab(tabType: TabType) =
        when (tabType) {
           TabType.Поиск -> R.id.searchFragment
           TabType.Медиатека -> R.id.mediaLibraryFragment
           TabType.Настройки -> R.id.settingsFragment
        }

}

enum class TabType {
    Поиск,
    Медиатека,
    Настройки
}