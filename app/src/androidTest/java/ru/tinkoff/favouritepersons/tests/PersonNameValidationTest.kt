package ru.tinkoff.favouritepersons.tests

import androidx.test.core.app.ActivityScenario
import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.tinkoff.favouritepersons.pageobjects.MainPage
import ru.tinkoff.favouritepersons.pageobjects.PersonItemPage
import ru.tinkoff.favouritepersons.presentation.activities.MainActivity
import ru.tinkoff.favouritepersons.testing.prefs.FavoritePersonsPrefs

class PersonNameValidationTest : BaseTest() {

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
    fun shouldAcceptValidName() = run {
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

        step("Вводим валидное имя и заполняем остальные поля") {
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

                fillName("Саша")

                clickSave()
            }
        }

        step("Проверяем что студент успешно добавился с именем Саша") {
            flakySafely(timeoutMs = 15000) {
                MainPage(this@run) {
                    checkScreenOpened()

                    assert(!isPersonListEmpty()) { "Student should be added" }

                    checkPersonAtPosition(0, "Саша", "Петров")
                }
            }
        }
    }

    @Test
    fun shouldRejectNameWithDigitsAndSpecialCharacters() = run {
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

        step("Вводим имя с цифрами и спецсимволами") {
            PersonItemPage(this) {
                fillName("Иван123!")

                fillSurname("Петров")
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

        step("Проверяем что появляется ошибка валидации для имени") {
            PersonItemPage(this) {
                flakySafely(timeoutMs = 5000) {
                    assert(isAddMode()) { "Should remain in add mode due to validation error" }

                    checkValidationErrorMessage("name", "Имя может содержать только буквы")
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
    fun shouldRejectEmptyName() = run {
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

        step("Оставляем поле имени пустым, заполняем остальные") {
            PersonItemPage(this) {
                fillSurname("Петров")
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

        step("Проверяем что появляется ошибка валидации для пустого имени") {
            PersonItemPage(this) {
                checkScreenOpened()

                assert(isAddMode()) { "Should remain in add mode due to empty name validation error" }

                checkValidationErrorMessage("name", "Поле должно быть заполнено!")
            }
        }
    }

    @Test
    fun shouldRejectNameWithOnlySpaces() = run {
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

        step("Вводим в поле имени только пробелы") {
            PersonItemPage(this) {
                fillName("   ")

                fillSurname("Петров")
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

        step("Проверяем что появляется ошибка валидации для имени из пробелов") {
            PersonItemPage(this) {
                checkScreenOpened()

                assert(isAddMode()) { "Should remain in add mode due to spaces-only name validation error" }

                checkValidationErrorMessage("name", "Поле должно быть заполнено!")
            }
        }
    }

    @Test
    fun shouldAcceptMinimumLengthName() = run {
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

        step("Вводим имя из одного символа") {
            PersonItemPage(this) {
                fillPersonData(
                    name = "И",
                    surname = "Петров",
                    gender = "М",
                    birthdate = "1995-01-01",
                    email = "i@example.com",
                    phone = "+79999999999",
                    address = "Тестовый адрес",
                    imageLink = "https://example.com/photo.jpg",
                    score = "85"
                )

                clickSave()
            }
        }

        step("Проверяем что студент успешно добавился с именем из одного символа") {
            flakySafely(timeoutMs = 15000) {
                MainPage(this@run) {
                    checkScreenOpened()

                    assert(!isPersonListEmpty()) { "Student should be added with single character name" }

                    checkPersonAtPosition(0, "И", "Петров")
                }
            }
        }
    }

    @Test
    fun shouldAcceptMaximumLengthName() = run {
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

        step("Вводим имя максимаальной длины (255 символов)") {
            PersonItemPage(this) {
                val longName = "А".repeat(255)
                fillName(longName)

                fillSurname("Длинноеимя")
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

        step("Проверяем что студент успешно добавился с длинным именем") {
            flakySafely(timeoutMs = 15000) {
                MainPage(this@run) {
                    checkScreenOpened()

                    assert(!isPersonListEmpty()) { "Student should be added with maximum length name" }

                    val expectedLongName = "А".repeat(255)

                    checkPersonAtPosition(0, expectedLongName, "Длинноеимя")
                }
            }
        }
    }
}
