package com.example.playlistmaker.screen.test

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.playlistmaker.screen.screen.elements.MainScreen
import com.example.playlistmaker.screen.screen.elements.MediaLibraryScreenElements
import com.example.playlistmaker.screen.screen.elements.TabType
import com.example.playlistmaker.ui.main.MainActivity
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class MediaLibraryTest : TestCase() {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun checkMediaLibrary() = run {
        step("Здесь мы указываем какая именно нам нужна страничка в навигации") {
            MainScreen {
                selectTab(getTabIdByTab(TabType.Медиатека))
                Thread.sleep(2000)
            }
        }
        step("Проверяем, что все элементы на месте") {
            MediaLibraryScreenElements{
                textMediaName { isDisplayed() }
                Thread.sleep(2000)
            }
        }
    }
}