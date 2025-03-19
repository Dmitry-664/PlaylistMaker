package com.example.playlistmaker.screen.test

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.playlistmaker.screen.screen.elements.MainScreen
import com.example.playlistmaker.screen.screen.elements.MainScreen.getTabIdByTab
import com.example.playlistmaker.screen.screen.elements.MainScreen.selectTab
import com.example.playlistmaker.screen.screen.elements.TabType
import com.example.playlistmaker.ui.main.MainActivity
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class MainTest : TestCase() {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun checkBottomNavigationView() = run {
        MainScreen {
            bottomNavigation { isDisplayed() }
        }
    }

    @Test
    fun clickTabOne() = run {
        clickTab(TabType.Поиск)
        Thread.sleep(2000)
        clickTab(TabType.Медиатека)
        Thread.sleep(2000)
        clickTab(TabType.Настройки)
        Thread.sleep(2000)
    }

    private fun clickTab(tabType: TabType) {
        selectTab(getTabIdByTab(tabType))
    }
}




