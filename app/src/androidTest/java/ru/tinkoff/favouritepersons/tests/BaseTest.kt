package ru.tinkoff.favouritepersons.tests

import androidx.test.platform.app.InstrumentationRegistry
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.params.FlakySafetyParams
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import ru.tinkoff.favouritepersons.room.PersonDataBase
import ru.tinkoff.favouritepersons.testing.rule.FavoritePersonsTestRule

/**
 * Базовый класс для всех тестов
 * Содержит общую конфигурацию Kaspresso и правила для тестов
 */
abstract class BaseTest(
    timeoutInMillis: Long = DEFAULT_TIMEOUT_MS,
    intervalInMillis: Long = DEFAULT_INTERVAL_MS
) : TestCase(
    kaspressoBuilder = Kaspresso.Builder.simple(
        customize = {
            flakySafetyParams = FlakySafetyParams.custom(
                timeoutMs = timeoutInMillis,
                intervalMs = intervalInMillis
            )
        }
    )
) {

    companion object {
        const val DEFAULT_TIMEOUT_MS = 10_000L
        const val DEFAULT_INTERVAL_MS = 500L
        const val WIREMOCK_PORT = 8080
    }

    @get:Rule
    val testRule = FavoritePersonsTestRule(isLocalhost = true)

    @get:Rule
    val wireMockRule = WireMockRule(WIREMOCK_PORT)

    /**
     * Очищает базу данных от всех студентов
     * Используется для обеспечения изоляции тестов
     */
    protected fun clearDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val database = PersonDataBase.getDBClient(context)
        runBlocking {
            try {
                database.clearAllTables()
            } catch (_: Exception) {
                // Fallback если clearAllTables() не работает
                database.query("DELETE FROM person", null)
            }
        }
    }

    /**
     * Обеспечивает чистое состояние для теста
     * Можно вызывать в setUp() или когда нужно гарантированно чистое состояние
     */
    protected fun ensureCleanState() {
        clearDatabase()
    }
}
