package ru.tinkoff.favouritepersons.tests

import androidx.test.core.app.ActivityScenario
import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.tinkoff.favouritepersons.pageobjects.MainPage
import ru.tinkoff.favouritepersons.pageobjects.PersonItemPage
import ru.tinkoff.favouritepersons.presentation.activities.MainActivity
import ru.tinkoff.favouritepersons.testing.prefs.FavoritePersonsPrefs

class BirthdateValidationTest : BaseTest() {

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
    fun shouldAcceptValidBirthdate() = run {
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

        step("Вводим валидную дату рождения и заполняем остальные поля") {
            PersonItemPage(this) {
                fillPersonData(
                    name = "Иван",
                    surname = "Петров",
                    gender = "М",
                    birthdate = "1990-12-31",
                    email = "ivan@example.com",
                    phone = "+79999999999",
                    address = "Тестовый адрес",
                    imageLink = "https://example.com/photo.jpg",
                    score = "85"
                )

                clickSave()
            }
        }

        step("Проверяем что студент успешно добавился с датой рождения 1990-12-31") {
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
    fun shouldRejectFutureBirthdate() = run {
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

        step("Вводим дату рождения в будущем") {
            PersonItemPage(this) {
                fillName("Иван")
                fillSurname("Петров")
                fillGender("М")
                fillBirthdate("2099-01-01")
                fillEmail("ivan@example.com")
                fillPhone("+79999999999")
                fillAddress("Тестовый адрес")
                fillImageLink("https://example.com/photo.jpg")
                fillScore("85")

                clickSave()
            }
        }

        step("Проверяем что появляется ошибка валидации для даты в будущем") {
            PersonItemPage(this) {
                flakySafely(timeoutMs = 5000) {
                    assert(isAddMode()) { "Should remain in add mode due to validation error" }

                    checkValidationErrorMessage("birthdate", "Дата рождения не может быть в будущем")
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
    fun shouldRejectIncorrectDateFormat() = run {
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

        step("Вводим дату рождения в неправильном формате") {
            PersonItemPage(this) {
                fillName("Иван")
                fillSurname("Петров")
                fillGender("М")
                fillBirthdate("31/12/1990")
                fillEmail("ivan@example.com")
                fillPhone("+79999999999")
                fillAddress("Тестовый адрес")
                fillImageLink("https://example.com/photo.jpg")
                fillScore("85")

                clickSave()
            }
        }

        step("Проверяем что появляется ошибка валидации для неправильного формата даты") {
            PersonItemPage(this) {
                flakySafely(timeoutMs = 5000) {
                    assert(isAddMode()) { "Should remain in add mode due to validation error" }

                    checkValidationErrorMessage("birthdate", "Поле должно быть заполнено в формате 1990-12-31")
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
    fun shouldRejectEmptyBirthdate() = run {
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

        step("Оставляем поле даты рождения пустым, заполняем остальные") {
            PersonItemPage(this) {
                fillName("Иван")
                fillSurname("Петров")
                fillGender("М")
                fillEmail("ivan@example.com")
                fillPhone("+79999999999")
                fillAddress("Тестовый адрес")
                fillImageLink("https://example.com/photo.jpg")
                fillScore("85")

                clickSave()
            }
        }

        step("Проверяем что появляется ошибка валидации для пустой даты рождения") {
            PersonItemPage(this) {
                flakySafely(timeoutMs = 5000) {
                    assert(isAddMode()) { "Should remain in add mode due to empty birthdate validation error" }

                    checkValidationErrorMessage("birthdate", "Поле должно быть заполнено в формате 1990-12-31")
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
    fun shouldAcceptDateFromDistantPast() = run {
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

        step("Вводим дату рождения из далекого прошлого") {
            PersonItemPage(this) {
                fillPersonData(
                    name = "Иван",
                    surname = "Петров",
                    gender = "М",
                    birthdate = "1900-01-01",
                    email = "ivan@example.com",
                    phone = "+79999999999",
                    address = "Тестовый адрес",
                    imageLink = "https://example.com/photo.jpg",
                    score = "85"
                )

                clickSave()
            }
        }

        step("Проверяем что студент успешно добавился с датой из далекого прошлого") {
            flakySafely(timeoutMs = 15000) {
                MainPage(this@run) {
                    checkScreenOpened()
                    assert(!isPersonListEmpty()) { "Student should be added with old birthdate" }
                    checkPersonAtPosition(0, "Иван", "Петров")
                }
            }
        }
    }

    @Test
    fun shouldAcceptCurrentDate() = run {
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

        step("Вводим текущую дату как дату рождения") {
            PersonItemPage(this) {
                fillPersonData(
                    name = "Иван",
                    surname = "Петров",
                    gender = "М",
                    birthdate = "2025-07-25",
                    email = "ivan@example.com",
                    phone = "+79999999999",
                    address = "Тестовый адрес",
                    imageLink = "https://example.com/photo.jpg",
                    score = "85"
                )

                clickSave()
            }
        }

        step("Проверяем что студент успешно добавился с текущей датой рождения") {
            flakySafely(timeoutMs = 15000) {
                MainPage(this@run) {
                    checkScreenOpened()
                    assert(!isPersonListEmpty()) { "Student should be added with current date as birthdate" }
                    checkPersonAtPosition(0, "Иван", "Петров")
                }
            }
        }
    }

    @Test
    fun shouldRejectInvalidDateValues() = run {
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

        step("Вводим невалидные значения даты") {
            PersonItemPage(this) {
                fillName("Иван")
                fillSurname("Петров")
                fillGender("М")
                fillBirthdate("1990-13-32")
                fillEmail("ivan@example.com")
                fillPhone("+79999999999")
                fillAddress("Тестовый адрес")
                fillImageLink("https://example.com/photo.jpg")
                fillScore("85")

                clickSave()
            }
        }

        step("Проверяем что появляется ошибка валидации для невалидной даты") {
            PersonItemPage(this) {
                flakySafely(timeoutMs = 5000) {
                    assert(isAddMode()) { "Should remain in add mode due to validation error" }

                    checkValidationErrorMessage("birthdate", "Поле должно быть заполнено в формате 1990-12-31")
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
}
