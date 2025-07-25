package ru.tinkoff.favouritepersons.testing.rule

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import ru.tinkoff.favouritepersons.testing.prefs.FavoritePersonsPrefs

/**
 * TestRule для настройки тестового окружения
 */
class FavoritePersonsTestRule(
    val isLocalhost: Boolean = false,
    val clearDataBefore: Boolean = true,
    val clearDataAfter: Boolean = true
) : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                if (clearDataBefore) {
                    FavoritePersonsPrefs.clear()
                }

                if (isLocalhost) {
                    FavoritePersonsPrefs.changeAppUrl()
                }

                try {
                    base.evaluate()
                } finally {
                    if (clearDataAfter) {
                        FavoritePersonsPrefs.clear()
                    }
                }
            }
        }
    }
}
