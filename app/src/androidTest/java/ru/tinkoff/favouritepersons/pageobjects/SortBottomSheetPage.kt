package ru.tinkoff.favouritepersons.pageobjects

import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import io.github.kakaocup.kakao.check.KCheckBox
import ru.tinkoff.favouritepersons.R

/**
 * PageObject для Bottom Sheet диалога сортировки
 */
class SortBottomSheetPage(testContext: TestContext<*>) : BaseScreen(testContext) {

    override val layoutId: Int = R.layout.bottom_sheet_dialog
    override val viewClass: Class<*>? = null

    private val sortByDefaultRadio = KCheckBox { withId(R.id.bsd_rb_default) }
    private val sortByAgeRadio = KCheckBox { withId(R.id.bsd_rb_age) }
    private val sortByRatingRadio = KCheckBox { withId(R.id.bsd_rb_rating) }
    private val sortByNameRadio = KCheckBox { withId(R.id.bsd_rb_name) }

    fun sortByDefault() {
        step("Сортируем по умолчанию") {
            sortByDefaultRadio.click()
        }
    }

    fun sortByAge() {
        step("Сортируем по возрасту") {
            sortByAgeRadio.click()
        }
    }

    fun sortByRating() {
        step("Сортируем по рейтингу") {
            sortByRatingRadio.click()
        }
    }

    fun sortByName() {
        step("Сортируем по имени/фамилии") {
            sortByNameRadio.click()
        }
    }

    fun checkDialogOpened() {
        step("Проверяем что диалог сортировки открылся") {
            Thread.sleep(2000)

            sortByDefaultRadio.isDisplayed()
        }
    }

    fun checkDialogOpenedSafely() {
        step("Безопасно проверяем что диалог сортировки открылся") {
            Thread.sleep(3000)

            sortByDefaultRadio.isDisplayed()
            sortByAgeRadio.isDisplayed()
            sortByRatingRadio.isDisplayed()
            sortByNameRadio.isDisplayed()
        }
    }

    fun checkAllSortOptionsVisible() {
        step("Проверяем что все опции сортировки видны") {
            sortByDefaultRadio.isDisplayed()
            sortByAgeRadio.isDisplayed()
            sortByRatingRadio.isDisplayed()
            sortByNameRadio.isDisplayed()
        }
    }

    companion object {
        const val SCREEN_NAME = "Диалог сортировки"

        inline operator fun invoke(testContext: TestContext<*>, crossinline block: SortBottomSheetPage.() -> Unit) {
            testContext.step(SCREEN_NAME) {
                SortBottomSheetPage(testContext).apply {
                    block()
                }
            }
        }
    }
}
