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


class MainScreenTest : BaseTest() {

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
    fun shouldShowEmptyStateWhenNoStudents() = run {
        step("Настраиваем пустой ответ от мока") {
            favoritePersonsMock {
                randomUser.respondWith(RandomUserResponseFactory.emptyResponse())
            }
        }

        step("Запускаем приложение") {
            ActivityScenario.launch(MainActivity::class.java)
        }

        step("Проверяем что отображается пустое состояние") {
            MainPage(this) {
                checkScreenOpened()
                assert(isPersonListEmpty()) { "List should be empty when no students" }
                checkNoPersonsTextVisible()
                checkFabVisible()
            }
        }
    }

    /**
     * T329 - Отображение карточек студентов при открытии приложения
     */
    @Test
    fun shouldDisplayStudentCardWithAllFields() = run {
        step("Настраиваем мок для студентки из тест-кейса") {
            favoritePersonsMock {
                randomUser.respondWith(RandomUserResponseFactory.femaleResponse())
            }
        }

        step("Запускаем приложение") {
            ActivityScenario.launch(MainActivity::class.java)
        }

        step("Добавляем студента и проверяем отображение всех полей карточки") {
            MainPage(this) {
                checkScreenOpened()
                addPersonFromNetwork()

                flakySafely(timeoutMs = 15000) {
                    assert(!isPersonListEmpty()) { "Student should be loaded from mock" }
                    assert(getPersonsCount() == 1) { "Should have exactly one student" }

                    checkPersonCardHasAllFields(0)

                    checkPersonCardComplete(0,
                        name = "Анна",
                        surname = "Петрова",
                        email = "anna.petrova@test.com",
                        phone = "+7 911 987 65 43",
                        address = "Ленинградская область, Санкт-Петербург, ул. Центральная - 456, Россия",
                        gender = "Female",
                        age = "38",
                        checkRating = true
                    )
                }
            }
        }
    }

    /**
     * T331 - Отображение FAB-кнопки
     */
    @Test
    fun shouldDisplayFabButton() = run {
        step("Запускаем приложение") {
            ActivityScenario.launch(MainActivity::class.java)
        }

        step("Проверяем отображение FAB кнопки в правом нижнем углу") {
            MainPage(this) {
                checkScreenOpened()
                checkFabVisible()
                checkFabClickable()
            }
        }
    }

    /**
     * Генерация карточки студента
     */
    @Test
    fun shouldAddStudentWhenFabClicked() = run {
        step("Настраиваем мок для студентки Jittie Tops из тест-кейса T333") {
            favoritePersonsMock {
                randomUser.respondWith(RandomUserResponseFactory.jittieTopsResponse())
            }
        }

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

        step("Нажимаем FAB и добавляем студентку Jittie Tops") {
            MainPage(this) {
                addPersonFromNetwork()

                flakySafely(timeoutMs = 15000) {
                    assert(!isPersonListEmpty()) { "Student should appear after FAB click" }
                    assert(getPersonsCount() == 1) { "Should have exactly one student" }

                    checkPersonAtPosition(0, "Jittie", "Tops")
                    checkPersonDetails(0, "Jittie", "Tops",
                        email = "jittie.tops@university.com",
                        phone = "+7 495 123 45 67")
                }
            }
        }
    }

    /**
     *  Открытие формы редактирования при нажатии на карточку студента
     */
    @Test
    fun shouldOpenEditFormWhenStudentCardClicked() = run {
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
                    checkPersonCardHasAllFields(0)
                }
            }
        }

        step("Нажимаем на карточку студента и проверяем открытие формы редактирования") {
            MainPage(this) {
                clickPersonAt(0)

                flakySafely(timeoutMs = 10000) {
                    PersonItemPage(this@run) {
                        checkScreenOpened()
                        assert(isEditMode()) { "Should open edit form when student card is clicked" }

                        checkPersonData(
                            name = "Алексей",
                            surname = "Смирнов",
                            email = "alex.smirnov@example.com",
                            phone = "+7 343 555 01 23"
                        )

                        checkFieldsEnabled()
                    }
                }
            }
        }
    }

    /**
     *  Удаление карточки свайпом влево
     */
    @Test
    fun shouldDeleteStudentWithSwipeLeft() = run {
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
                    assert(getPersonsCount() == 1) { "Should have one student before deletion" }
                    checkPersonAtPosition(0, "Галина", "Сидорова")
                }
            }
        }

        step("Удаляем студента свайпом слева направо") {
            MainPage(this) {
                deletePersonAt(0)

                flakySafely(timeoutMs = 10000) {
                    assert(isPersonListEmpty()) { "Student should be deleted after swipe" }
                    checkNoPersonsTextVisible()
                }
            }
        }
    }

    /**
     *  Обработка ошибок сети
     */
    @Test
    fun shouldHandleNetworkErrors() = run {
        step("Настраиваем мок для ошибки сети") {
            favoritePersonsMock {
                randomUser.respondWith(500, RandomUserResponseFactory.errorResponse())
            }
        }

        step("Запускаем приложение") {
            ActivityScenario.launch(MainActivity::class.java)
        }

        step("Пытаемся добавить студента при ошибке сети") {
            MainPage(this) {
                checkScreenOpened()
                addPersonFromNetwork()

                flakySafely(timeoutMs = 10000) {
                    assert(isPersonListEmpty()) { "List should remain empty when network error occurs" }

                    checkNoPersonsTextVisible()
                    checkFabVisible()
                }
            }
        }
    }



}
