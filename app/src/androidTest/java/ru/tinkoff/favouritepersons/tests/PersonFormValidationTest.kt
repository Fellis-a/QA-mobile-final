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
class PersonFormValidationTest : BaseTest() {

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

    @Test
    fun shouldHandleBrokenImageLink() = run {
        step("Запускаем приложение") {
            ActivityScenario.launch(MainActivity::class.java)
        }

        step("Открываем форму добавления нового студента") {
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
        }

        step("Вставляем невалидную ссылку на изображение") {
            PersonItemPage(this) {
                fillImageLink("http://broken.link/image.jpg")

                fillPersonData(
                    name = "Тест",
                    surname = "Тестов",
                    gender = "М",
                    birthdate = "1995-01-01",
                    email = "test@example.com",
                    phone = "+79999999999",
                    address = "Тестовый адрес",
                    score = "85"
                )

                clickSave()
            }
        }

        step("Проверяем что студент добавился без ошибок") {
            flakySafely(timeoutMs = 15000) {
                MainPage(this@run) {
                    checkScreenOpened()
                    assert(!isPersonListEmpty()) { "Student should be added successfully" }
                    assert(getPersonsCount() == 1) { "Should have exactly one student" }

                    checkPersonAtPosition(0, "Тест", "Тестов")
                    checkPersonDetails(0, "Тест", "Тестов",
                        email = "test@example.com",
                        phone = "+79999999999")
                }
            }
        }
    }

    @Test
    fun shouldShowErrorForEmptyRequiredFields() = run {
        step("Запускаем приложение") {
            ActivityScenario.launch(MainActivity::class.java)
        }

        step("Проверяем начальное пустое состояние") {
            MainPage(this) {
                checkScreenOpened()

                assert(isPersonListEmpty()) { "List should be empty initially" }

                checkNoPersonsTextVisible()
            }
        }

        step("Открываем форму добавления студента") {
            MainPage(this) {
                addPersonManually()

                flakySafely(timeoutMs = 10000) {
                    PersonItemPage(this@run) {
                        checkScreenOpened()

                        assert(isAddMode()) { "Should open in add mode" }
                    }
                }
            }
        }

        step("Пытаемся сохранить пустую форму и проверяем ошибки валидации") {
            PersonItemPage(this) {
                clickSave()

                flakySafely(timeoutMs = 5000) {
                    assert(isAddMode()) { "Should remain in add mode after validation failure" }

                    checkValidationError("name")
                    checkValidationError("surname")
                    checkValidationError("gender")
                    checkValidationError("birthdate")
                    checkValidationError("email")
                    checkValidationError("phone")
                    checkValidationError("address")
                    checkValidationError("imagelink")
                    checkValidationError("score")
                }

                pressBack()
            }
        }

        step("Проверяем что данные не сохранились в список студентов") {
            flakySafely(timeoutMs = 10000) {
                MainPage(this@run) {
                    checkScreenOpened()

                    assert(isPersonListEmpty()) { "List should remain empty after validation failure" }

                    checkNoPersonsTextVisible()

                    assert(getPersonsCount() == 0) { "Should have zero students after failed validation" }
                }
            }
        }
    }
}
