package ru.tinkoff.favouritepersons.pageobjects

import com.kaspersky.kaspresso.screens.KScreen
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.kaspersky.kaspresso.testcases.models.info.StepInfo

/**
 * Базовый класс для всех экранов приложения
 */
abstract class BaseScreen(protected val testContext: TestContext<*>) : KScreen<BaseScreen>() {

    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    fun step(description: String, actions: (StepInfo) -> Unit) {
        testContext.step(description, actions)
    }
}
