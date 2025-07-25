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
class EmailValidationTest : BaseTest() {

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
    fun shouldAcceptValidEmail() = run {
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

        step("Вводим валидный e-mail адрес и заполняем остальные поля") {
            PersonItemPage(this) {
                fillPersonData(
                    name = "Иван",
                    surname = "Петров",
                    gender = "М",
                    birthdate = "1995-01-01",
                    email = "user@example.com",
                    phone = "+79999999999",
                    address = "Тестовый адрес",
                    imageLink = "https://example.com/photo.jpg",
                    score = "85"
                )

                clickSave()
            }
        }

        step("Проверяем что студент успешно добавился с e-mail user@example.com") {
            flakySafely(timeoutMs = 15000) {
                MainPage(this@run) {
                    checkScreenOpened()
                    assert(!isPersonListEmpty()) { "Student should be added" }
                    checkPersonAtPosition(0, "Иван", "Петров")
                    checkPersonDetails(
                        0, "Иван", "Петров",
                        email = "user@example.com",
                        phone = "+79999999999"
                    )
                }
            }
        }
    }

    @Test
    fun shouldRejectEmailWithoutAtSymbol() = run {
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

        step("Заполняем форму с e-mail без символа @") {
            PersonItemPage(this) {
                fillName("Иван")
                fillSurname("Петров")
                fillGender("М")
                fillBirthdate("1995-01-01")
                fillEmail("userexample.com")
                fillPhone("+79999999999")
                fillAddress("Тестовый адрес")
                fillImageLink("https://example.com/photo.jpg")
                fillScore("85")

                clickSave()
            }
        }

        step("Проверяем что появляется ошибка валидации для e-mail без @") {
            PersonItemPage(this) {
                flakySafely(timeoutMs = 5000) {
                    assert(isAddMode()) { "Should remain in add mode due to validation error" }
                    checkValidationErrorMessage(
                        "email",
                        "Поле должно быть заполнено в формате mail@gmail.com"
                    )
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
    fun shouldRejectEmailWithoutDomain() = run {
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

        step("Заполняем форму с e-mail без доменной зоны") {
            PersonItemPage(this) {
                fillName("Иван")
                fillSurname("Петров")
                fillGender("М")
                fillBirthdate("1995-01-01")
                fillEmail("user@example")
                fillPhone("+79999999999")
                fillAddress("Тестовый адрес")
                fillImageLink("https://example.com/photo.jpg")
                fillScore("85")

                clickSave()
            }
        }

        step("Проверяем что появляется ошибка валидации для e-mail без доменной зоны") {
            PersonItemPage(this) {
                flakySafely(timeoutMs = 5000) {
                    assert(isAddMode()) { "Should remain in add mode due to validation error" }

                    checkValidationErrorMessage(
                        "email",
                        "Поле должно быть заполнено в формате mail@gmail.com"
                    )
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
    fun shouldRejectEmailWithCyrillic() = run {
        step("Запускаем приложение") {
            ActivityScenario.launch(MainActivity::class.java)
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

        step("Заполняем форму с e-mail на кириллице") {
            PersonItemPage(this) {
                fillName("Иван")
                fillSurname("Петров")
                fillGender("М")
                fillBirthdate("1995-01-01")
                fillEmail("тест@почта.рф")
                fillPhone("+79999999999")
                fillAddress("Тестовый адрес")
                fillImageLink("https://example.com/photo.jpg")
                fillScore("85")

                clickSave()
            }
        }

        step("Проверяем что появляется ошибка валидации для e-mail с кириллицей") {
            PersonItemPage(this) {
                flakySafely(timeoutMs = 5000) {
                    assert(isAddMode()) { "Should remain in add mode due to validation error" }

                    checkValidationErrorMessage(
                        "email",
                        "Поле должно быть заполнено в формате mail@gmail.com"
                    )
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
    fun shouldRejectEmailWithSpaces() = run {
        step("Запускаем приложение") {
            ActivityScenario.launch(MainActivity::class.java)
        }

        step("Проверяем начальное пустое состояние") {
            MainPage(this) {
                checkScreenOpened()

                assert(isPersonListEmpty()) { "List should be empty initially" }
                step("Запускаем приложение") {
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

            step("Заполняем форму с e-mail содержащим пробелы") {
                PersonItemPage(this) {
                    fillName("Иван")
                    fillSurname("Петров")
                    fillGender("М")
                    fillBirthdate("1995-01-01")
                    fillEmail("user @example.com")
                    fillPhone("+79999999999")
                    fillAddress("Тестовый адрес")
                    fillImageLink("https://example.com/photo.jpg")
                    fillScore("85")

                    clickSave()
                }
            }

            step("Проверяем что появляется ошибка валидации для e-mail с пробелами") {
                PersonItemPage(this) {
                    flakySafely(timeoutMs = 5000) {
                        assert(isAddMode()) { "Should remain in add mode due to validation error" }

                        checkValidationErrorMessage(
                            "email",
                            "Поле должно быть заполнено в формате mail@gmail.com"
                        )
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

        @Test
        fun shouldRejectEmptyEmail() = run {
            step("Запускаем приложение") {
                ActivityScenario.launch(MainActivity::class.java)
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

            step("Оставляем поле e-mail пустым, заполняем остальные") {
                PersonItemPage(this) {
                    fillName("Иван")
                    fillSurname("Петров")
                    fillGender("М")
                    fillBirthdate("1995-01-01")
                    fillPhone("+79999999999")
                    fillAddress("Тестовый адрес")
                    fillImageLink("https://example.com/photo.jpg")
                    fillScore("85")

                    clickSave()
                }
            }

            step("Проверяем что появляется ошибка валидации для пустого e-mail") {
                PersonItemPage(this) {
                    flakySafely(timeoutMs = 5000) {
                        assert(isAddMode()) { "Should remain in add mode due to empty email validation error" }

                        checkValidationErrorMessage(
                            "email",
                            "Поле должно быть заполнено в формате mail@gmail.com"
                        )
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

    }}