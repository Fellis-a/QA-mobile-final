package ru.tinkoff.favouritepersons.tests

import androidx.test.core.app.ActivityScenario
import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.tinkoff.favouritepersons.pageobjects.MainPage
import ru.tinkoff.favouritepersons.pageobjects.PersonItemPage
import ru.tinkoff.favouritepersons.presentation.activities.MainActivity
import ru.tinkoff.favouritepersons.testing.prefs.FavoritePersonsPrefs

class ScoreValidationTest : BaseTest() {

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
    fun shouldAcceptValidScore() = run {
        step("Запускаем приложение") {
            ActivityScenario.launch(MainActivity::class.java)
        }

        step("Открываем форму добавления студента") {
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

        step("Вводим валидный итоговый балл и заполняем остальные поля") {
            PersonItemPage(this) {
                fillPersonData(
                    name = "Иван",
                    surname = "Петров",
                    gender = "М",
                    birthdate = "1995-01-01",
                    email = "ivan@example.com",
                    phone = "+79999999999",
                    address = "Тестовый адрес",
                    imageLink = "https://example.com/photo.jpg",
                    score = "85"
                )

                clickSave()
            }
        }

        step("Проверяем что студент успешно добавился с итоговым баллом 85") {
            flakySafely(timeoutMs = 15000) {
                MainPage(this@run) {
                    checkScreenOpened()

                    assert(!isPersonListEmpty()) { "Student should be added" }

                    checkPersonAtPosition(0, "Иван", "Петров")
                }
            }
        }
    }

    @Test
    fun shouldRejectNegativeScore() = run {
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

        step("Заполняем форму с отрицательным итоговым баллом") {
            PersonItemPage(this) {
                fillName("Иван")
                fillSurname("Петров")
                fillGender("М")
                fillBirthdate("1995-01-01")
                fillEmail("ivan@example.com")
                fillPhone("+79999999999")
                fillAddress("Тестовый адрес")
                fillImageLink("https://example.com/photo.jpg")
                fillScore("-5")

                clickSave()
            }
        }

        step("Проверяем что появляется ошибка валидации для отрицательного балла") {
            PersonItemPage(this) {
                flakySafely(timeoutMs = 5000) {
                    assert(isAddMode()) { "Should remain in add mode due to validation error" }

                    checkValidationErrorMessage("score", "Поле должно быть заполнено двузначным числом")
                }

                pressBack()
            }
        }

        step("Проверяем что данные НЕ сохранились в список студентов") {
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

    @Test
    fun shouldRejectThreeDigitScore() = run {
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

        step("Заполняем форму с трёхзначным итоговым баллом") {
            PersonItemPage(this) {
                fillName("Иван")
                fillSurname("Петров")
                fillGender("М")
                fillBirthdate("1995-01-01")
                fillEmail("ivan@example.com")
                fillPhone("+79999999999")
                fillAddress("Тестовый адрес")
                fillImageLink("https://example.com/photo.jpg")
                fillScore("101")

                clickSave()
            }
        }

        step("Проверяем что появляется ошибка валидации для трёхзначного балла") {
            PersonItemPage(this) {
                flakySafely(timeoutMs = 5000) {
                    assert(isAddMode()) { "Should remain in add mode due to validation error" }

                    checkValidationErrorMessage("score", "Поле должно быть заполнено двузначным числом")
                }

                pressBack()
            }
        }

        step("Проверяем что данные НЕ сохранились в список студентов") {
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

    @Test
    fun shouldRejectSingleDigitScore() = run {
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

        step("Заполняем форму с однозначным итоговым баллом") {
            PersonItemPage(this) {
                fillName("Иван")
                fillSurname("Петров")
                fillGender("М")
                fillBirthdate("1995-01-01")
                fillEmail("ivan@example.com")
                fillPhone("+79999999999")
                fillAddress("Тестовый адрес")
                fillImageLink("https://example.com/photo.jpg")
                fillScore("9")

                clickSave()
            }
        }

        step("Проверяем что появляется ошибка валидации для однозначного балла") {
            PersonItemPage(this) {
                flakySafely(timeoutMs = 5000) {
                    assert(isAddMode()) { "Should remain in add mode due to validation error" }

                    checkValidationErrorMessage("score", "Поле должно быть заполнено двузначным числом")
                }

                pressBack()
            }
        }

        step("Проверяем что данные НЕ сохранились в список студентов") {
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

    @Test
    fun shouldRejectTextInScoreField() = run {
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

        step("Заполняем форму с текстом в поле итогового балла") {
            PersonItemPage(this) {
                fillName("Иван")
                fillSurname("Петров")
                fillGender("М")
                fillBirthdate("1995-01-01")
                fillEmail("ivan@example.com")
                fillPhone("+79999999999")
                fillAddress("Тестовый адрес")
                fillImageLink("https://example.com/photo.jpg")
                fillScore("пятьдесят")

                clickSave()
            }
        }

        step("Проверяем что появляется ошибка валидации текста в поле балла") {
            PersonItemPage(this) {
                flakySafely(timeoutMs = 5000) {
                    assert(isAddMode()) { "Should remain in add mode due to validation error" }

                    checkValidationErrorMessage("score", "Поле должно быть заполнено двузначным числом")
                }

                pressBack()
            }
        }

        step("Проверяем что данные НЕ сохранились в список студентов") {
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

    @Test
    fun shouldRejectDecimalScore() = run {
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

        step("Заполняем форму с дробным итоговым баллом") {
            PersonItemPage(this) {
                fillName("Иван")
                fillSurname("Петров")
                fillGender("М")
                fillBirthdate("1995-01-01")
                fillEmail("ivan@example.com")
                fillPhone("+79999999999")
                fillAddress("Тестовый адрес")
                fillImageLink("https://example.com/photo.jpg")
                fillScore("85.5")

                clickSave()
            }
        }

        step("Проверяем что появляется ошибка валидации для дробного балла") {
            PersonItemPage(this) {
                flakySafely(timeoutMs = 5000) {
                    assert(isAddMode()) { "Should remain in add mode due to validation error" }

                    checkValidationErrorMessage("score", "Поле должно быть заполнено двузначным числом")
                }

                pressBack()
            }
        }

        step("Проверяем что данные НЕ сохранились в список студентов") {
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

    @Test
    fun shouldRejectEmptyScore() = run {
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

        step("Оставляем поле итогового балла пустым, заполняем остальные") {
            PersonItemPage(this) {
                fillName("Иван")
                fillSurname("Петров")
                fillGender("М")
                fillBirthdate("1995-01-01")
                fillEmail("ivan@example.com")
                fillPhone("+79999999999")
                fillAddress("Тестовый адрес")
                fillImageLink("https://example.com/photo.jpg")

                clickSave()
            }
        }

        step("Проверяем что появляется ошибка валидации для пустого поля балла") {
            PersonItemPage(this) {
                flakySafely(timeoutMs = 5000) {
                    assert(isAddMode()) { "Should remain in add mode due to empty score validation error" }

                    checkValidationErrorMessage("score", "Поле должно быть заполнено двузначным числом")
                }

                pressBack()
            }
        }

        step("Проверяем что данные НЕ сохранились в список студентов") {
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

    @Test
    fun shouldAcceptMinimumValidScore() = run {
        step("Запускаем приложение") {
            ActivityScenario.launch(MainActivity::class.java)
        }

        step("Открываем форму добавления студента") {
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

        step("Вводим минимальный валидный итоговый балл (10)") {
            PersonItemPage(this) {
                fillPersonData(
                    name = "Иван",
                    surname = "Петров",
                    gender = "М",
                    birthdate = "1995-01-01",
                    email = "ivan@example.com",
                    phone = "+79999999999",
                    address = "Тестовый адрес",
                    imageLink = "https://example.com/photo.jpg",
                    score = "10"
                )

                clickSave()
            }
        }

        step("Проверяем что студент успешно добавился с баллом 10") {
            flakySafely(timeoutMs = 15000) {
                MainPage(this@run) {
                    checkScreenOpened()

                    assert(!isPersonListEmpty()) { "Student should be added with minimum valid score" }

                    checkPersonAtPosition(0, "Иван", "Петров")
                }
            }
        }
    }

    @Test
    fun shouldAcceptMaximumValidScore() = run {
        step("Запускаем приложение") {
            ActivityScenario.launch(MainActivity::class.java)
        }

        step("Открываем форму добавления студента") {
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

        step("Вводим максимальный валидный итоговый балл (99)") {
            PersonItemPage(this) {
                fillPersonData(
                    name = "Иван",
                    surname = "Петров",
                    gender = "М",
                    birthdate = "1995-01-01",
                    email = "ivan@example.com",
                    phone = "+79999999999",
                    address = "Тестовый адрес",
                    imageLink = "https://example.com/photo.jpg",
                    score = "99"
                )

                clickSave()
            }
        }

        step("Проверяем что студент успешно добавился с баллом 99") {
            flakySafely(timeoutMs = 15000) {
                MainPage(this@run) {
                    checkScreenOpened()

                    assert(!isPersonListEmpty()) { "Student should be added with maximum valid score" }

                    checkPersonAtPosition(0, "Иван", "Петров")
                }
            }
        }
    }
}
