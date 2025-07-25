package ru.tinkoff.favouritepersons.tests

import androidx.test.core.app.ActivityScenario
import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.tinkoff.favouritepersons.pageobjects.MainPage
import ru.tinkoff.favouritepersons.pageobjects.PersonItemPage
import ru.tinkoff.favouritepersons.presentation.activities.MainActivity
import ru.tinkoff.favouritepersons.testing.mock.FavoritePersonsMock.favoritePersonsMock
import ru.tinkoff.favouritepersons.testing.mock.response.RandomUserResponseFactory
import ru.tinkoff.favouritepersons.testing.prefs.FavoritePersonsPrefs


class PersonItemFormTest : BaseTest() {

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
    fun shouldOpenEditFormWithPrefilledFields() = run {
        step("Настраиваем мок и добавляем студента") {
            favoritePersonsMock {
                randomUser.respondWith(RandomUserResponseFactory.femaleResponse())
            }
        }

        step("Запускаем приложение и добавляем студента") {
            ActivityScenario.launch(MainActivity::class.java)

            MainPage(this) {
                checkScreenOpened()
                addPersonFromNetwork()

                flakySafely(timeoutMs = 15000) {
                    assert(getPersonsCount() == 1) { "Should have one student" }

                    checkPersonAtPosition(0, "Анна", "Петрова")
                }
            }
        }

        step("Нажимаем на карточку студента для редактирования") {
            MainPage(this) {
                clickPersonAt(0)

                flakySafely(timeoutMs = 10000) {
                    PersonItemPage(this@run) {
                        checkScreenOpened()
                        assert(isEditMode()) { "Should open in edit mode" }

                        checkPersonData(
                            name = "Анна",
                            surname = "Петрова",
                            email = "anna.petrova@test.com",
                            phone = "+7 911 987 65 43"
                        )

                        checkFieldsEnabled()
                    }
                }
            }
        }
    }

    @Test
    fun shouldAddStudentManually() = run {
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

        step("Открываем форму ручного добавления студента") {
            MainPage(this) {
                addPersonManually()

                flakySafely(timeoutMs = 10000) {
                    PersonItemPage(this@run) {
                        checkScreenOpened()

                        assert(isAddMode()) { "Should open in add mode" }

                        checkFieldsEnabled()
                    }
                }
            }
        }

        step("Заполняем форму данными нового студента") {
            PersonItemPage(this) {
                fillPersonData(
                    name = "Анна",
                    surname = "Иванова",
                    gender = "Ж",
                    birthdate = "2000-01-10",
                    email = "anna@example.com",
                    phone = "+79999999999",
                    address = "Москва, ул. Пушкина, д.10",
                    imageLink = "https://example.com/photo.jpg",
                    score = "85"
                )

                clickSave()
            }
        }

        step("Проверяем что студент добавился на главный экран") {
            flakySafely(timeoutMs = 15000) {
                MainPage(this@run) {
                    assert(!isPersonListEmpty()) { "Student should be added to the list" }

                    assert(getPersonsCount() == 1) { "Should have exactly one student" }

                    checkPersonAtPosition(0, "Анна", "Иванова")
                    checkPersonDetails(0, "Анна", "Иванова",
                        email = "anna@example.com",
                        phone = "+79999999999")
                }
            }
        }
    }

    @Test
    fun shouldEditStudentName() = run {
        step("Настраиваем мок и добавляем студента") {
            favoritePersonsMock {
                randomUser.respondWith(RandomUserResponseFactory.successResponse())
            }
        }

        step("Запускаем приложение и добавляем студента") {
            ActivityScenario.launch(MainActivity::class.java)

            MainPage(this) {
                checkScreenOpened()

                addPersonFromNetwork()

                flakySafely(timeoutMs = 15000) {
                    assert(getPersonsCount() == 1) { "Should have one student" }
                    checkPersonAtPosition(0, "Иван", "Иванов")
                }
            }
        }

        step("Открываем форму редактирования и изменяем имя") {
            MainPage(this) {
                clickPersonAt(0)

                flakySafely(timeoutMs = 10000) {
                    PersonItemPage(this@run) {
                        checkScreenOpened()

                        assert(isEditMode()) { "Should open in edit mode" }

                        fillName("Мария")

                        clickSave()
                    }
                }
            }
        }

        step("Проверяем что имя обновилось на главном экране") {
            flakySafely(timeoutMs = 15000) {
                MainPage(this@run) {
                    checkPersonAtPosition(0, "Мария", "Иванов")
                }
            }
        }
    }

    @Test
    fun shouldEditAllStudentFields() = run {
        step("Настраиваем мок и добавляем студента") {
            favoritePersonsMock {
                randomUser.respondWith(RandomUserResponseFactory.youngMaleResponse())
            }
        }

        step("Запускаем приложение и добавляем студента") {
            ActivityScenario.launch(MainActivity::class.java)

            MainPage(this) {
                checkScreenOpened()

                addPersonFromNetwork()

                flakySafely(timeoutMs = 15000) {
                    assert(getPersonsCount() == 1) { "Should have one student" }
                }
            }
        }

        step("Открываем форму редактирования и изменяем все поля") {
            MainPage(this) {
                clickPersonAt(0)

                flakySafely(timeoutMs = 10000) {
                    PersonItemPage(this@run) {
                        checkScreenOpened()
                        assert(isEditMode()) { "Should open in edit mode" }

                        fillPersonData(
                            name = "Екатерина",
                            surname = "Смирнова",
                            gender = "Ж",
                            birthdate = "1995-06-15",
                            email = "ekaterina@newdomain.com",
                            phone = "+78888888888",
                            address = "Санкт-Петербург, Невский пр., д.25",
                            imageLink = "https://newdomain.com/new-photo.jpg",
                            score = "95"
                        )

                        clickSave()
                    }
                }
            }
        }

        step("Проверяем что все изменения сохранились") {
            flakySafely(timeoutMs = 15000) {
                MainPage(this@run) {
                    checkPersonAtPosition(0, "Екатерина", "Смирнова")
                    checkPersonDetails(0, "Екатерина", "Смирнова",
                        email = "ekaterina@newdomain.com",
                        phone = "+78888888888")
                }
            }
        }
    }

    @Test
    fun shouldKeepSaveButtonDisabledWithoutChanges() = run {
        step("Настраиваем мок и добавляем студента") {
            favoritePersonsMock {
                randomUser.respondWith(RandomUserResponseFactory.elderlyFemaleResponse())
            }
        }

        step("Запускаем приложение и добавляем студента") {
            ActivityScenario.launch(MainActivity::class.java)

            MainPage(this) {
                checkScreenOpened()
                addPersonFromNetwork()

                flakySafely(timeoutMs = 15000) {
                    assert(getPersonsCount() == 1) { "Should have one student" }
                }
            }
        }

        step("Открываем форму редактирования без внесения изменений") {
            MainPage(this) {
                clickPersonAt(0)

                flakySafely(timeoutMs = 10000) {
                    PersonItemPage(this@run) {
                        checkScreenOpened()
                        assert(isEditMode()) { "Should open in edit mode" }

                        checkPersonData(
                            name = "Галина",
                            surname = "Сидорова",
                            email = "galina.sidorova@mail.ru",
                            phone = "+7 383 777 88 99"
                        )

                        checkSaveButtonDisabled()

                        val wasButtonEnabled = isSaveButtonEnabled()

                        assert(!wasButtonEnabled) { "Save button should be disabled when no changes are made" }
                    }
                }
            }
        }
    }

    @Test
    fun shouldValidateRequiredFields() = run {
        step("Запускаем приложение") {
            ActivityScenario.launch(MainActivity::class.java)
        }

        step("Открываем форму ручного добавления студента") {
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

        step("Пытаемся сохранить пустую форму") {
            PersonItemPage(this) {
                clickSave()

                checkValidationErrorMessage("name", "Поле должно быть заполнено!")
                checkValidationErrorMessage("surname", "Поле должно быть заполнено!")
                checkValidationErrorMessage("gender", "Поле должно быть заполнено буквами М или Ж")
                checkValidationErrorMessage("birthdate", "Поле должно быть заполнено в формате 1990-12-31")
                checkValidationErrorMessage("email", "Поле должно быть заполнено в формате mail@gmail.com")
                checkValidationErrorMessage("phone", "Поле должно быть заполнено!")
                checkValidationErrorMessage("address", "Поле должно быть заполнено!")
                checkValidationErrorMessage("score", "Поле должно быть заполнено двузначным числом")
                checkValidationErrorMessage("imagelink", "Поле должно быть заполнено!")
            }
        }

        step("Заполняем только одно поле и проверяем что остальные поля по-прежнему показывают ошибки") {
            PersonItemPage(this) {
                fillName("Тест")

                clickSave()

                checkNoValidationErrors()

                checkValidationErrorMessage("surname", "Поле должно быть заполнено!")
                checkValidationErrorMessage("gender", "Поле должно быть заполнено буквами М или Ж")
                checkValidationErrorMessage("birthdate", "Поле должно быть заполнено в формате 1990-12-31")
                checkValidationErrorMessage("email", "Поле должно быть заполнено в формате mail@gmail.com")
                checkValidationErrorMessage("phone", "Поле должно быть заполнено!")
                checkValidationErrorMessage("address", "Поле должно быть заполнено!")
                checkValidationErrorMessage("score", "Поле должно быть заполнено двузначным числом")
                checkValidationErrorMessage("imagelink", "Поле должно быть заполнено!")
            }
        }
    }


    @Test
    fun shouldNavigateBackWithoutSaving() = run {
        step("Настраиваем мок и добавляем студента") {
            favoritePersonsMock {
                randomUser.respondWith(RandomUserResponseFactory.femaleResponse())
            }
        }

        step("Запускаем приложение и добавляем студента") {
            ActivityScenario.launch(MainActivity::class.java)

            MainPage(this) {
                checkScreenOpened()
                addPersonFromNetwork()

                flakySafely(timeoutMs = 15000) {
                    assert(getPersonsCount() == 1) { "Should have one student" }
                    checkPersonAtPosition(0, "Анна", "Петрова")
                }
            }
        }

        step("Открываем форму редактирования и изменяем данные без сохранения") {
            MainPage(this) {
                clickPersonAt(0)

                flakySafely(timeoutMs = 10000) {
                    PersonItemPage(this@run) {
                        checkScreenOpened()
                        assert(isEditMode()) { "Should open in edit mode" }

                        fillName("НовоеИмя")
                        fillSurname("НоваяФамилия")
                        fillEmail("newemail@test.com")

                        minimizeApp()
                    }
                }
            }
        }

        step("Возвращаемся в приложение и проверяем сохранение состояния формы") {
            PersonItemPage(this) {
                reopenApp()

                flakySafely(timeoutMs = 10000) {
                    checkScreenOpened()

                    checkPersonData(
                        name = "НовоеИмя",
                        surname = "НоваяФамилия",
                        email = "newemail@test.com"
                    )
                }

                goBackSafely()
            }
        }

        step("Проверяем что изменения не сохранились в списке") {
            flakySafely(timeoutMs = 10000) {
                MainPage(this@run) {
                    checkScreenOpened()

                    checkPersonAtPosition(0, "Анна", "Петрова")
                }
            }
        }
    }

    @Test
    fun shouldRetainUnsavedDataWhenAddingStudent() = run {
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

        step("Открываем форму ручного добавления студента") {
            MainPage(this) {
                addPersonManually()

                flakySafely(timeoutMs = 10000) {
                    PersonItemPage(this@run) {
                        checkScreenOpened()

                        assert(isAddMode()) { "Should open in add mode" }

                        checkFieldsEnabled()
                    }
                }
            }
        }

        step("Заполняем форму данными, но не сохраняем") {
            PersonItemPage(this) {
                fillPersonData(
                    name = "Тестовое",
                    surname = "Имя",
                    gender = "М",
                    birthdate = "1995-03-15",
                    email = "test.user@example.com",
                    phone = "+79123456789",
                    address = "Москва, ул. Тестовая, д.42",
                    imageLink = "https://example.com/test-photo.jpg",
                    score = "92"
                )

                minimizeApp()
            }
        }

        step("Возвращаемся в приложение и проверяем сохранение введенных данных") {
            PersonItemPage(this) {
                reopenApp()

                flakySafely(timeoutMs = 10000) {
                    checkScreenOpened()
                    assert(isAddMode()) { "Should still be in add mode" }

                    checkPersonData(
                        name = "Тестовое",
                        surname = "Имя",
                        gender = "М",
                        birthdate = "1995-03-15",
                        email = "test.user@example.com",
                        phone = "+79123456789",
                        address = "Москва, ул. Тестовая, д.42",
                        imageLink = "https://example.com/test-photo.jpg",
                        score = "92"
                    )
                }

                clickSave()

                pressBack()
            }
        }

        step("Проверяем что студент успешно добавился в список") {
            flakySafely(timeoutMs = 15000) {
                MainPage(this@run) {
                    checkScreenOpened()
                    assert(!isPersonListEmpty()) { "Student should be added to the list" }
                    assert(getPersonsCount() == 1) { "Should have exactly one student" }

                    checkPersonAtPosition(0, "Тестовое", "Имя")
                    checkPersonDetails(0, "Тестовое", "Имя",
                        email = "test.user@example.com",
                        phone = "+79123456789")
                }
            }
        }
    }

    @Test
    fun shouldRetainPartialDataWhenAddingStudent() = run {
        step("Запускаем приложение") {
            ActivityScenario.launch(MainActivity::class.java)
        }

        step("Открываем форму ручного добавления студента") {
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

        step("Заполняем только часть полей") {
            PersonItemPage(this) {
                fillName("Частично")
                fillSurname("Заполненный")
                fillEmail("partial@test.com")

                minimizeApp()
            }
        }

        step("Возвращаемся в приложение и проверяем частично введенные данные") {
            PersonItemPage(this) {
                reopenApp()

                flakySafely(timeoutMs = 10000) {
                    checkScreenOpened()
                    assert(isAddMode()) { "Should still be in add mode" }

                    checkPersonData(
                        name = "Частично",
                        surname = "Заполненный",
                        email = "partial@test.com"
                    )
                }

                clickSave()

                checkScreenOpened()

                assert(isAddMode()) { "Should still be in add mode after failed validation" }

                fillGender("М")
                fillBirthdate("1990-01-01")
                fillPhone("+79111111111")
                fillAddress("Дозаполненный адрес")
                fillImageLink("https://example.com/photo.jpg")
                fillScore("85")

                clickSave()
            }
        }

        step("Проверяем что студент добавился только после заполнения всех обязательных полей") {
            flakySafely(timeoutMs = 15000) {
                MainPage(this@run) {
                    assert(!isPersonListEmpty()) { "Student should be added only after all required fields are filled" }

                    checkPersonAtPosition(0, "Частично", "Заполненный")
                    checkPersonDetails(0, "Частично", "Заполненный",
                        email = "partial@test.com",
                        phone = "+79111111111")
                }
            }
        }
    }
}
