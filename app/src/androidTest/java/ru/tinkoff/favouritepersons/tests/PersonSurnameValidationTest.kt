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
class PersonSurnameValidationTest : BaseTest() {

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
    fun shouldAcceptValidSurname() = run {
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

        step("Вводим валидную фамилию и заполняем остальные поля") {
            PersonItemPage(this) {
                fillPersonData(
                    name = "Иван",
                    surname = "Иванов",
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

        step("Проверяем что студент успешно добавился с фамилией Иванов") {
            flakySafely(timeoutMs = 15000) {
                MainPage(this@run) {
                    checkScreenOpened()

                    assert(!isPersonListEmpty()) { "Student should be added" }

                    checkPersonAtPosition(0, "Иван", "Иванов")
                }
            }
        }
    }

    @Test
    fun shouldRejectSurnameWithDigitsAndSpecialCharacters() = run {
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

        step("Вводим фамилию с цифрами и спецсимволами") {
            PersonItemPage(this) {
                fillName("Иван")
                fillSurname("Иванов123?")

                fillGender("М")
                fillBirthdate("1995-01-01")
                fillEmail("ivan@example.com")
                fillPhone("+79999999999")
                fillAddress("Тестовый адрес")
                fillImageLink("https://example.com/photo.jpg")
                fillScore("85")

                clickSave()
            }
        }

        step("Проверяем что появляется ошибка валидации для фамилии") {
            PersonItemPage(this) {
                flakySafely(timeoutMs = 5000) {
                    assert(isAddMode()) { "Should remain in add mode due to validation error" }

                    checkValidationErrorMessage("surname", "Поле может содержать только буквы")
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
    fun shouldRejectEmptySurname() = run {
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

        step("Оставляем поле фамилии пустым, заполняем остальные") {
            PersonItemPage(this) {
                fillName("Иван")

                fillGender("М")
                fillBirthdate("1995-01-01")
                fillEmail("test@example.com")
                fillPhone("+79999999999")
                fillAddress("Тестовый адрес")
                fillImageLink("https://example.com/photo.jpg")
                fillScore("85")

                clickSave()
            }
        }

        step("Проверяем что появляется ошибка валидации для пустой фамилии") {
            PersonItemPage(this) {
                flakySafely(timeoutMs = 5000) {
                    assert(isAddMode()) { "Should remain in add mode due to empty surname validation error" }

                    checkValidationErrorMessage("surname", "Поле должно быть заполнено!")
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
    fun shouldRejectSurnameWithOnlySpaces() = run {
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

        step("Вводим в поле фамилии только пробелы") {
            PersonItemPage(this) {
                fillName("Иван")
                fillSurname("   ")

                fillGender("М")
                fillBirthdate("1995-01-01")
                fillEmail("test@example.com")
                fillPhone("+79999999999")
                fillAddress("Тестовый адрес")
                fillImageLink("https://example.com/photo.jpg")
                fillScore("85")

                clickSave()
            }
        }

        step("Проверяем что появляется ошибка валидации для фамилии из пробелов") {
            PersonItemPage(this) {
                flakySafely(timeoutMs = 5000) {
                    assert(isAddMode()) { "Should remain in add mode due to spaces-only surname validation error" }

                    checkValidationErrorMessage("surname", "Поле должно быть заполнено!")
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
    fun shouldAcceptMinimumLengthSurname() = run {
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

        step("Вводим фамилию из одного символа") {
            PersonItemPage(this) {
                fillPersonData(
                    name = "Иван",
                    surname = "И",
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

        step("Проверяем что студент успешно добавился с фамилией из одного символа") {
            flakySafely(timeoutMs = 15000) {
                MainPage(this@run) {
                    checkScreenOpened()

                    assert(!isPersonListEmpty()) { "Student should be added with single character surname" }

                    checkPersonAtPosition(0, "Иван", "И")
                }
            }
        }
    }

    @Test
    fun shouldAcceptMaximumLengthSurname() = run {
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

        step("Вводим фамилию максимальной длины (255 символов)") {
            PersonItemPage(this) {
                val longSurname = "А".repeat(255)

                fillName("Иван")
                fillSurname(longSurname)
                fillGender("М")
                fillBirthdate("1995-01-01")
                fillEmail("longname@example.com")
                fillPhone("+79999999999")
                fillAddress("Тестовый адрес")
                fillImageLink("https://example.com/photo.jpg")
                fillScore("85")

                clickSave()
            }
        }

        step("Проверяем что студент успешно добавился с длинной фамилией") {
            flakySafely(timeoutMs = 15000) {
                MainPage(this@run) {
                    checkScreenOpened()

                    assert(!isPersonListEmpty()) { "Student should be added with maximum length surname" }

                    val expectedLongSurname = "А".repeat(255)

                    checkPersonAtPosition(0, "Иван", expectedLongSurname)
                }
            }
        }
    }
}
