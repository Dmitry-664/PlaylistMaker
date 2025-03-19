package com.example.playlistmaker.screen.test

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.playlistmaker.screen.screen.elements.MainScreen
import com.example.playlistmaker.screen.screen.elements.SettingsScreenElements
import com.example.playlistmaker.screen.screen.elements.TabType
import com.example.playlistmaker.ui.main.MainActivity
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class SettingsTest : TestCase() {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun checkSettingsTest() = run {

        step("Нажимаем кнопку Настройки") {
            MainScreen {
                selectTab(getTabIdByTab(TabType.Настройки))
                Thread.sleep(2000)
            }
        }
        // Проверяем, что все элементы UI присутствуют
        step("Verify all settings screen elements are displayed") {
            SettingsScreenElements {
                headerSettingsName { isDisplayed() }
                darkThemeSettingsName { isDisplayed() }
                Thread.sleep(2000)
                shareSettingsName { isDisplayed() }
                writeSettingsName { isDisplayed() }
                Thread.sleep(2000)
                userAgreementName { isDisplayed() }
                switchButton { isDisplayed() }
                buttonShare { isDisplayed() }
                writeToSupport { isDisplayed() }
                userAgreement { isDisplayed() }
            }
        }
    }
}

        // Проверяем, что переключатель "Темная тема" работает
//        step("Turn on dark theme") {
//            SettingsScreen {
//                switchButton { isChecked() }
//            }
//        }
//        step("Turn off dark theme") {
//            SettingsScreen {
//                switchButton { isNotChecked() }
//            }
//
//            // Проверяем, что кнопки "Поделиться", "Написать в поддержку" и "Пользовательское соглашение" работают
//            step("Click share button") {
//                SettingsScreen {
//                    buttonShare { click() }
//                    // Здесь можно добавить проверку, что действие "Поделиться" было выполнено
//                }
//            }
//            step("Click write to support button") {
//                SettingsScreen {
//                    writeToSupport { click() }
//                    // Здесь можно добавить проверку, что действие "Написать в поддержку" было выполнено
//                }
//            }
//            step("Click user agreement button") {
//                SettingsScreen {
//                    userAgreement { click() }
//                    // Здесь можно добавить проверку, что действие "Пользовательское соглашение" было выполнено
//                }
//            }
//        }
//    }


