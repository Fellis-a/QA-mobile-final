package ru.tinkoff.favouritepersons.testing.prefs

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.core.content.edit

/**
 * Утилита для работы с настройками приложения в тестах
 */
object FavoritePersonsPrefs {

    private val prefs = InstrumentationRegistry
        .getInstrumentation()
        .targetContext
        .getSharedPreferences("demo_url", Context.MODE_PRIVATE)

    fun changeAppUrl(url: String = "http://localhost:8080/") {
        prefs.edit { putString("url", url) }
    }

    fun setMockMode(enabled: Boolean = true) {
        prefs.edit { putBoolean("mock_mode", enabled) }
    }

    fun clear() {
        prefs.edit { clear() }
    }
}
