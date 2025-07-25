package ru.tinkoff.favouritepersons.tests

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.tinkoff.favouritepersons.pageobjects.MainPage
import ru.tinkoff.favouritepersons.pageobjects.PersonItemPage
import ru.tinkoff.favouritepersons.presentation.activities.MainActivity
import ru.tinkoff.favouritepersons.testing.prefs.FavoritePersonsPrefs


@RunWith(AndroidJUnit4::class)
class StudentDeletionPersistenceTest : BaseTest() {

    @Before
    fun setUp() {
        FavoritePersonsPrefs.changeAppUrl("http://localhost:8080/")
        FavoritePersonsPrefs.setMockMode(true)
        ensureCleanState()
    }

    @After
    fun tearDown() {
        FavoritePersonsPrefs.clear()
        clearDatabase()
    }

    /**
     *  Отсутствие удалённого студента после перезапуска
     */
    @Test
    fun shouldNotShowDeletedStudentAfterAppRestart() = run {
        step("Добавляем студента для последующего удаления") {
            ActivityScenario.launch(MainActivity::class.java)

            MainPage(this) {
                checkScreenOpened()
                addPersonManually()

                flakySafely(timeoutMs = 10000) {
                    PersonItemPage(this@run) {
                        checkScreenOpened()
                        assert(isAddMode()) { "Should open in add mode" }
                    }
                }
            }

            PersonItemPage(this) {
                fillPersonData(
                    name = "Тестовый",
                    surname = "Студент",
                    gender = "М",
                    birthdate = "1995-01-01",
                    email = "test.student@example.com",
                    phone = "+79999999999",
                    address = "Тестовый адрес",
                    imageLink = "https://example.com/photo.jpg",
                    score = "85"
                )

                clickSave()
            }

            flakySafely(timeoutMs = 15000) {
                MainPage(this@run) {
                    checkScreenOpened()

                    assert(!isPersonListEmpty()) { "Student should be added" }

                    assert(getPersonsCount() == 1) { "Should have exactly one student" }

                    checkPersonAtPosition(0, "Тестовый", "Студент")
                }
            }
        }

        step("Удаляем студента свайпом слева направо") {
            MainPage(this) {
                deletePersonAt(0)

                flakySafely(timeoutMs = 10000) {
                    assert(isPersonListEmpty()) { "Student should be deleted from the list" }

                    assert(getPersonsCount() == 0) { "Should have zero students after deletion" }

                    checkNoPersonsTextVisible()
                }
            }
        }

        var scenario: ActivityScenario<MainActivity>? = null

        step("Перезапускаем приложение") {
            scenario?.close()
            scenario = ActivityScenario.launch(MainActivity::class.java)
        }

        step("Проверяем что удалённый студент не отображается после перезапуска") {
            flakySafely(timeoutMs = 15000) {
                MainPage(this@run) {
                    checkScreenOpened()

                    assert(isPersonListEmpty()) { "Deleted student should not appear after app restart" }

                    assert(getPersonsCount() == 0) { "Should still have zero students after restart" }

                    checkNoPersonsTextVisible()
                }
            }
        }

        scenario?.close()
    }
}