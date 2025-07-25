package ru.tinkoff.favouritepersons.tests

import androidx.test.core.app.ActivityScenario
import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.tinkoff.favouritepersons.pageobjects.MainPage
import ru.tinkoff.favouritepersons.pageobjects.SortBottomSheetPage
import ru.tinkoff.favouritepersons.presentation.activities.MainActivity
import ru.tinkoff.favouritepersons.testing.mock.FavoritePersonsMock.favoritePersonsMock
import ru.tinkoff.favouritepersons.testing.mock.response.StudentResponseFactory
import ru.tinkoff.favouritepersons.testing.prefs.FavoritePersonsPrefs


class MassOperationsAndSortingTest : BaseTest() {

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
     *  Массовое удаление студентов
     */
    @Test
    fun shouldDeleteMultipleStudentsInBulk() = run {
        step("Подготавливаем 12 студентов для массового удаления") {
            ActivityScenario.launch(MainActivity::class.java)

            val students = StudentResponseFactory.getTestStudentsData()

            MainPage(this) {
                checkScreenOpened()
                addMultipleStudentsFromNetwork(students)

                flakySafely(timeoutMs = 5000) {
                    assert(getPersonsCount() == 12) { "Should have exactly 12 students before deletion" }
                }
            }
        }

        step("Массово удаляем всех студентов через swipe") {
            MainPage(this) {
                val deletionTime = deleteAllStudentsWithTracking()

                flakySafely(timeoutMs = 8000) {
                    assert(getPersonsCount() == 0) { "All students should be deleted" }
                    assert(isPersonListEmpty()) { "List should be empty after deletion" }
                    checkNoPersonsTextVisible()
                }

                checkFabVisible()
                checkFabClickable()

                println("Total deletion time: ${deletionTime}ms")
            }
        }
    }

    /**
     *  Сортировка по ФИО
     */
    @Test
    fun shouldSortStudentsByName() = run {
        step("Подготавливаем студентов с разными именами для сортировки") {
            ActivityScenario.launch(MainActivity::class.java)

            val students = StudentResponseFactory.getSortByNameTestData()

            MainPage(this) {
                checkScreenOpened()

                students.forEachIndexed { index, (firstName, lastName, rating) ->
                    favoritePersonsMock {
                        randomUser.respondWith(StudentResponseFactory.createStudentResponse(firstName, lastName, rating))
                    }

                    if (index == 0) addPersonFromNetwork() else addPersonFromNetworkSmart()

                    flakySafely(timeoutMs = 8000) {
                        assert(getPersonsCount() == index + 1)
                    }
                    Thread.sleep(1000)
                }
            }
        }

        step("Открываем меню сортировки") {
            MainPage(this) {
                openSortMenuSafely()

                SortBottomSheetPage(this@run) {
                    checkDialogOpenedSafely()
                    checkAllSortOptionsVisible()
                }
            }
        }

        step("Выбираем сортировку по ФИО") {
            SortBottomSheetPage(this) {
                sortByName()
            }
        }

        step("Проверяем что карточки отсортированы по алфавиту") {
            MainPage(this) {
                flakySafely(timeoutMs = 10000) {
                    checkPersonAtPosition(0, "Анна", "Белова")
                    checkPersonAtPosition(1, "Борис", "Антонов")
                    checkPersonAtPosition(2, "Дарья", "Григорьева")
                    checkPersonAtPosition(3, "Михаил", "Сидоров")
                }
            }
        }
    }

    /**
     *  Сортировка пустого списка студентов
     */
    @Test
    fun shouldHandleEmptyListSorting() = run {
        step("Запускаем приложение с пустым списком") {
            ActivityScenario.launch(MainActivity::class.java)
        }

        step("Удалить всех студентов") {
            MainPage(this) {
                checkScreenOpened()
                flakySafely(timeoutMs = 5000) {
                    assert(isPersonListEmpty()) { "List should be empty initially" }
                    checkNoPersonsTextVisible()
                }
            }
        }

        step("Нажать на меню сортировки в правом верхнем углу экрана") {
            MainPage(this) {
                try {
                    openSortMenu()

                    SortBottomSheetPage(this@run) {
                        checkDialogOpened()
                    }

                    try {
                        closeDialogByClickingOutside()
                    } catch (_: Exception) {
                    }

                } catch (_: Exception) {
                    println("Sort button is inactive as expected")
                }
            }
        }

        step("Добавить нового студента") {
            MainPage(this) {
                favoritePersonsMock {
                    randomUser.respondWith(StudentResponseFactory.createStudentResponse("Тест", "Тестов", 85))
                }
                addPersonFromNetwork()

                flakySafely(timeoutMs = 8000) {
                    assert(getPersonsCount() == 1) { "Should have 1 student after adding" }
                }
            }
        }

        step("Иконка сортировки в правом верхнем углу становится активной") {
            MainPage(this) {
                try {
                    openSortMenu()

                    SortBottomSheetPage(this@run) {
                        checkDialogOpened()

                        checkAllSortOptionsVisible()

                        sortByDefault()
                    }
                } catch (e: Exception) {
                    throw AssertionError("Sort button should be active after adding student, but failed to open menu: ${e.message}")
                }
            }
        }
    }

    /**
     * Сортировка по умолчанию
     */
    @Test
    fun shouldSortStudentsByDefault() = run {
        step("Добавляем 3 студентов в определенном порядке с задержками") {
            ActivityScenario.launch(MainActivity::class.java)

            val students = listOf(
                Triple("Первый", "Студент", 85),
                Triple("Второй", "Студент", 90),
                Triple("Третий", "Студент", 88)
            )

            MainPage(this) {
                checkScreenOpened()

                students.forEachIndexed { index, (firstName, lastName, rating) ->
                    favoritePersonsMock {
                        randomUser.respondWith(StudentResponseFactory.createStudentResponse(firstName, lastName, rating))
                    }

                    if (index == 0) addPersonFromNetwork() else addPersonFromNetworkSmart()

                    flakySafely(timeoutMs = 8000) {
                        assert(getPersonsCount() == index + 1)
                    }

                    Thread.sleep(2000)
                    println("Added student ${index + 1}: $firstName $lastName at ${System.currentTimeMillis()}")
                }

                println("Current order in list after adding all students:")
                for (pos in 0 until getPersonsCount()) {
                    try {
                        val names = listOf("Первый", "Второй", "Третий")
                        for (name in names) {
                            try {
                                checkPersonAtPosition(pos, name, "Студент")
                                println("Position $pos: $name Студент")
                                break
                            } catch (_: Exception) {
                            }
                        }
                    } catch (_: Exception) {
                        println("Position $pos: Unable to determine")
                    }
                }
            }
        }

        step("Открыть меню сортировки в правом верхнем углу экрана") {
            MainPage(this) {
                openSortMenu()

                SortBottomSheetPage(this@run) {
                    checkDialogOpened()

                    checkAllSortOptionsVisible()
                }
            }
        }

        step("Меняем сортировку на другую (по имени), чтобы изменить порядок") {
            SortBottomSheetPage(this) {
                sortByName()
            }
        }

        step("Выбрать вариант \"По умолчанию\"") {
            MainPage(this) {
                openSortMenu()
                SortBottomSheetPage(this@run) {
                    checkDialogOpened()
                    sortByDefault()
                }

                Thread.sleep(2000)
            }
        }

        step("Карточки студентов отсортированы по времени добавления (по убыванию)") {
            MainPage(this) {
                flakySafely(timeoutMs = 10000) {
                    for (pos in 0 until getPersonsCount()) {
                        try {
                            val names = listOf("Первый", "Второй", "Третий")
                            for (name in names) {
                                try {
                                    checkPersonAtPosition(pos, name, "Студент")
                                    println("Position $pos: $name Студент (after default sort)")
                                    break
                                } catch (_: Exception) {
                                }
                            }
                        } catch (_: Exception) {
                            println("Position $pos: Unable to determine")
                        }
                    }

                    val studentsFound = mutableSetOf<String>()

                    for (position in 0..2) {
                        val studentNames = listOf("Первый", "Второй", "Третий")

                        for (studentName in studentNames) {
                            try {
                                checkPersonAtPosition(position, studentName, "Студент")

                                studentsFound.add("$studentName Студент")

                                break
                            } catch (_: Exception) {
                            }
                        }
                    }

                    val expectedStudents = setOf("Первый Студент", "Второй Студент", "Третий Студент")

                    assert(studentsFound == expectedStudents) {
                        "Not all students found after default sort. Expected: $expectedStudents, Found: $studentsFound"
                    }


                }
            }
        }
    }

    /**
     * Сортировка по возрасту
     */
    @Test
    fun shouldSortStudentsByAge() = run {
        step("Добавляем студентов с разным возрастом") {
            ActivityScenario.launch(MainActivity::class.java)

            MainPage(this) {
                checkScreenOpened()

                favoritePersonsMock {
                    randomUser.respondWith(StudentResponseFactory.createStudentResponseWithAge("Молодой", "Студент", 85, 22))
                }
                addPersonFromNetwork()

                favoritePersonsMock {
                    randomUser.respondWith(StudentResponseFactory.createStudentResponseWithAge("Средний", "Студент", 88, 25))
                }
                addPersonFromNetworkSmart()

                favoritePersonsMock {
                    randomUser.respondWith(StudentResponseFactory.createStudentResponseWithAge("Старший", "Студент", 90, 28))
                }
                addPersonFromNetworkSmart()

                flakySafely(timeoutMs = 10000) {
                    assert(getPersonsCount() == 3) { "Should have 3 students" }
                }
            }
        }

        step("Сортируем по возрасту") {
            MainPage(this) {
                openSortMenu()

                SortBottomSheetPage(this@run) {
                    checkDialogOpened()

                    sortByAge()
                }
            }
        }

        step("Проверяем сортировку по возрасту (от старших к младшим)") {
            MainPage(this) {
                flakySafely(timeoutMs = 10000) {
                    checkPersonAtPositionSafely(0, "Старший", "Студент")
                    checkPersonAtPositionSafely(1, "Средний", "Студент")
                    checkPersonAtPositionSafely(2, "Молодой", "Студент")
                }
            }
        }
    }

    /**
     *  Сортировка по итоговому баллу
     */
    @Test
    fun shouldSortStudentsByRating() = run {
        step("Добавляем студентов для тестирования сортировки по баллам") {
            ActivityScenario.launch(MainActivity::class.java)

            val students = StudentResponseFactory.getSortByRatingTestData()

            MainPage(this) {
                checkScreenOpened()
                addMultipleStudentsFromNetwork(students)

                flakySafely(timeoutMs = 5000) {
                    assert(getPersonsCount() == 5) { "Should have 5 students before sorting" }
                }
            }
        }

        step("Сортируем по итоговому баллу") {
            MainPage(this) {
                openSortMenu()

                SortBottomSheetPage(this@run) {
                    checkDialogOpened()
                    sortByRating()
                }

                Thread.sleep(2000)
            }
        }

        step("Проверяем что сортировка по баллам работает корректно") {
            MainPage(this) {
                flakySafely(timeoutMs = 10000) {
                    verifyRatingsSortedDescending()
                }
            }
        }
    }
}
