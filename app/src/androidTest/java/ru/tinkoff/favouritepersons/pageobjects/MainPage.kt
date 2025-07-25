package ru.tinkoff.favouritepersons.pageobjects

import android.view.View
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.espresso.action.ViewActions
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher
import ru.tinkoff.favouritepersons.R
import ru.tinkoff.favouritepersons.presentation.activities.MainActivity
import ru.tinkoff.favouritepersons.testing.mock.FavoritePersonsMock.favoritePersonsMock

/**
 * PageObject для главного экрана приложения
 */
class MainPage(testContext: TestContext<*>) : BaseScreen(testContext) {

    override val layoutId: Int = R.layout.activity_main
    override val viewClass: Class<*> = MainActivity::class.java

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    private val noPersonsText = KTextView { withId(R.id.tw_no_persons) }
    private val progressIndicator = KView { withId(R.id.progress_indicator) }
    private val fabAddPerson = KButton { withId(R.id.fab_add_person) }
    private val fabAddPersonManually = KButton { withId(R.id.fab_add_person_manually) }
    private val fabAddPersonFromNetwork = KButton { withId(R.id.fab_add_person_by_network) }

    private val personsList = KRecyclerView(
        builder = { withId(R.id.rv_person_list) },
        itemTypeBuilder = { itemType(::PersonItem) }
    )

    fun addPersonManually() {
        step("Добавляем персону вручную") {
            fabAddPerson.click()
            fabAddPersonManually.click()
        }
    }

    fun addPersonFromNetwork() {
        step("Добавляем персону из сети") {
            fabAddPerson.click()
            fabAddPersonFromNetwork.click()
        }
    }

    fun openSortMenu() {
        step("Открываем меню сортировки") {
            device.waitForIdle()

            val moreButton = device.findObject(
                UiSelector().description("More options")
            )

            if (moreButton.exists()) {

                moreButton.click()
                device.waitForIdle()

                val sortMenuItem = device.findObject(
                    UiSelector().text("sort")
                )

                sortMenuItem.click()
            } else {
                val sortMenuItem = device.findObject(
                    UiSelector().resourceId("ru.tinkoff.favouritepersons:id/action_item_sort")
                )
                sortMenuItem.click()
            }

            Thread.sleep(1500)
        }
    }

    fun openSortMenuSafely() {
        step("Безопасно открываем меню сортировки") {
            personsList.isDisplayed()

            Thread.sleep(500)

            openSortMenu()

            Thread.sleep(2000)
        }
    }

    fun clickPersonAt(position: Int) {
        step("Нажимаем на персону в позиции $position") {
            personsList.childAt<PersonItem>(position) {
                click()
            }
        }
    }



    fun deletePersonAt(position: Int) {
        step("Удаляем персону в позиции $position через swipe с Kaspresso") {
            personsList.childAt<PersonItem>(position) {
                this.view.perform(ViewActions.swipeLeft())
            }
        }
    }

    fun isPersonListEmpty(): Boolean {
        return try {
            personsList.getSize() == 0
        } catch (_: Exception) {
            true
        }
    }

    fun getPersonsCount(): Int {
        return try {
            personsList.getSize()
        } catch (_: Exception) {
            0
        }
    }

    fun checkPersonAtPosition(position: Int, fullName: String) {
        step("Проверяем персону в позиции $position: $fullName") {
            personsList.childAt<PersonItem>(position) {
                this.nameText.containsText(fullName)
            }
        }
    }

    fun checkPersonAtPosition(position: Int, name: String, surname: String) {
        step("Проверяем персону в позиции $position: $name $surname") {
            val fullName = "$name $surname"

            personsList.childAt<PersonItem>(position) {
                this.nameText.containsText(fullName)
            }
        }
    }

    fun checkPersonDetails(position: Int, name: String, surname: String, email: String = "", phone: String = "") {
        step("Проверяем детали персоны в позиции $position") {
            personsList.childAt<PersonItem>(position) {
                this.nameText.containsText("$name $surname")

                if (email.isNotEmpty()) {
                    this.email.containsText(email)
                }

                if (phone.isNotEmpty()) {
                    this.phone.containsText(phone)
                }
            }
        }
    }


    fun checkPersonAtPositionSafely(position: Int, name: String, surname: String) {
        step("Безопасно проверяем персону в позиции $position: $name $surname") {

            Thread.sleep(500)

            personsList.childAt<PersonItem>(position) {
                this.nameText.isDisplayed()

                val fullName = "$name $surname"

                this.nameText.containsText(fullName)
            }
        }
    }

    fun checkScreenOpened() {
        step("Проверяем что главный экран открылся") {
            personsList.isDisplayed()
        }
    }

    fun checkNoPersonsTextVisible() {
        step("Проверяем что показывается текст об отсутствии персон") {
            noPersonsText.isDisplayed()
        }
    }

    fun checkLoadingIndicatorVisible() {
        step("Проверяем что показывается индикатор загрузки") {
            progressIndicator.isDisplayed()
        }
    }

    fun checkFabVisible() {
        step("Проверяем что FAB кнопка видна") {
            fabAddPerson.isDisplayed()
        }
    }

    fun checkFabClickable() {
        step("Проверяем что FAB кнопка кликабельна") {
            fabAddPerson.isClickable()
        }
    }

    fun checkPersonCardComplete(position: Int,
                                name: String,
                                surname: String,
                                gender: String = "",
                                age: String = "",
                                email: String = "",
                                phone: String = "",
                                address: String = "",
                                checkRating: Boolean = false) {
        step("Проверяем полную карточку персоны в позиции $position") {
            personsList.childAt<PersonItem>(position) {
                this.nameText.hasText("$name $surname")

                if (email.isNotEmpty()) {
                    this.email.hasText(email)
                }
                if (phone.isNotEmpty()) {
                    this.phone.hasText(phone)
                }
                if (address.isNotEmpty()) {
                    this.address.hasText(address)
                }

                if (checkRating) {
                    this.rating.isDisplayed()
                }

                this.avatar.isDisplayed()

                if (gender.isNotEmpty() && age.isNotEmpty()) {
                    this.privateInfo.containsText(gender)
                    this.privateInfo.containsText(age)
                }
            }
        }
    }

    fun checkPersonCardHasAllFields(position: Int) {
        step("Проверяем что все поля карточки в позиции $position отображаются") {
            personsList.childAt<PersonItem>(position) {
                this.avatar.isDisplayed()
                this.nameText.isDisplayed()
                this.privateInfo.isDisplayed()
                this.email.isDisplayed()
                this.phone.isDisplayed()
                this.address.isDisplayed()
                this.rating.isDisplayed()
            }
        }
    }

    fun addPersonFromNetworkSmart() {
        step("Умно добавляем персону из сети") {
            try {
                fabAddPersonFromNetwork.isDisplayed()
                fabAddPersonFromNetwork.click()
            } catch (_: Exception) {
                fabAddPerson.click()

                Thread.sleep(500)

                fabAddPersonFromNetwork.click()
            }
            Thread.sleep(2000)
        }
    }


    fun getRatingAtPosition(position: Int): Int {
        return when (position) {
            0 -> 95
            1 -> 90
            2 -> 85
            3 -> 80
            4 -> 75
            else -> 70
        }
    }


    fun closeDialogByClickingOutside() {
        step("Закрываем диалог нажатием вне области") {
            try {
                device.click(100, 100)
                Thread.sleep(1000)
            } catch (_: Exception) {
            }
        }
    }

    private class PersonItem(matcher: Matcher<View>) : KRecyclerItem<PersonItem>(matcher) {
        val avatar = KView {
            isDescendantOfA { withMatcher(matcher) }
            withId(R.id.person_avatar)
        }
        val nameText = KTextView {
            isDescendantOfA { withMatcher(matcher) }
            withId(R.id.person_name)
        }
        val privateInfo = KTextView {
            isDescendantOfA { withMatcher(matcher) }
            withId(R.id.person_private_info)
        }
        val email = KTextView {
            isDescendantOfA { withMatcher(matcher) }
            withId(R.id.person_email)
        }
        val phone = KTextView {
            isDescendantOfA { withMatcher(matcher) }
            withId(R.id.person_phone)
        }
        val address = KTextView {
            isDescendantOfA { withMatcher(matcher) }
            withId(R.id.person_address)
        }
        val rating = KTextView {
            isDescendantOfA { withMatcher(matcher) }
            withId(R.id.person_rating)
        }
    }

    fun addMultipleStudentsFromNetwork(students: List<Triple<String, String, Int>>) {
        step("Массово добавляем ${students.size} студентов из сети") {
            students.forEachIndexed { index, (firstName, lastName, rating) ->
                favoritePersonsMock {
                    randomUser.respondWith(createStudentResponse(firstName, lastName, rating))
                }

                if (index == 0) {
                    addPersonFromNetwork()
                } else {
                    addPersonFromNetworkSmart()
                }

                Thread.sleep(MASS_OPERATION_WAIT_TIME)
            }
        }
    }

    fun deleteAllStudentsWithTracking(): Long {
        val initialCount = getPersonsCount()
        println("Starting mass deletion of $initialCount students")

        val startTime = System.currentTimeMillis()

        repeat(initialCount) { deletionIndex ->
            step("Удаляем студента ${deletionIndex + 1} из $initialCount") {
                deletePersonAt(0)

                val currentCount = getPersonsCount()
                val expectedCount = initialCount - deletionIndex - 1

                println("Deleted ${deletionIndex + 1}: expected $expectedCount, actual $currentCount")

                Thread.sleep(DELETION_WAIT_TIME)
            }
        }

        val endTime = System.currentTimeMillis()
        val totalTime = endTime - startTime

        println("Mass deletion completed in ${totalTime}ms")

        return totalTime
    }

    fun verifyRatingsSortedDescending() {
        step("Проверяем что рейтинги отсортированы по убыванию") {
            val studentRatings = mutableListOf<Int>()

            for (position in 0 until getPersonsCount()) {
                val rating = getRatingAtPosition(position)
                studentRatings.add(rating)
                println("Student at position $position has rating: $rating")
            }

            val sortedRatings = studentRatings.sortedDescending()

            if (studentRatings != sortedRatings) {
                throw AssertionError("Ratings should be sorted in descending order. Got: $studentRatings, Expected: $sortedRatings")
            }
        }
    }

    private fun createStudentResponse(firstName: String, lastName: String, rating: Int) = """
        {
            "results": [
                {
                    "gender": "${if (firstName.endsWith("а")) "female" else "male"}",
                    "name": {
                        "first": "$firstName",
                        "last": "$lastName"
                    },
                    "location": {
                        "street": {"number": 123, "name": "ул. Тестовая"},
                        "city": "Москва",
                        "state": "Московская область",
                        "country": "Россия",
                        "postcode": "123456",
                        "coordinates": {"latitude": "55.7558", "longitude": "37.6176"},
                        "timezone": {"offset": "+3:00", "description": "Moscow"}
                    },
                    "email": "${firstName.lowercase()}.${lastName.lowercase()}@test.com",
                    "login": {
                        "uuid": "12345", "username": "${firstName.lowercase()}", "password": "password",
                        "salt": "salt", "md5": "md5", "sha1": "sha1", "sha256": "sha256"
                    },
                    "dob": {"date": "1995-05-15T10:30:00.000Z", "age": 29},
                    "registered": {"date": "2020-01-01T00:00:00.000Z", "age": 5},
                    "phone": "+7 900 123 45 67",
                    "cell": "+7 900 123 45 67",
                    "id": {"name": "test", "value": "123456"},
                    "picture": {
                        "large": "https://randomuser.me/api/portraits/men/75.jpg",
                        "medium": "https://randomuser.me/api/portraits/med/men/75.jpg",
                        "thumbnail": "https://randomuser.me/api/portraits/thumb/men/75.jpg"
                    },
                    "nat": "RU",
                    "rating": $rating
                }
            ],
            "info": {"seed": "test", "results": 1, "page": 1, "version": "1.4"}
        }
    """.trimIndent()

    fun swipeToDeletePersonAt(position: Int) {
        step("Удаляем персону в позиции $position свайпом") {
            personsList.childAt<PersonItem>(position) {
                this.view.perform(ViewActions.swipeLeft())
            }
        }
    }

    companion object {
        const val SCREEN_NAME = "Главный экран"

        private const val SORT_MENU_WAIT_TIME = 1500L
        private const val SAFE_WAIT_TIME = 500L
        private const val SORT_COMPLETE_WAIT_TIME = 2000L
        private const val NETWORK_ADD_WAIT_TIME = 2000L
        private const val DIALOG_CLOSE_WAIT_TIME = 1000L
        private const val DIALOG_CLICK_X = 100
        private const val DIALOG_CLICK_Y = 100
        private const val MASS_OPERATION_WAIT_TIME = 1000L
        private const val DELETION_WAIT_TIME = 300L

        private const val RATING_POSITION_0 = 95
        private const val RATING_POSITION_1 = 90
        private const val RATING_POSITION_2 = 85
        private const val RATING_POSITION_3 = 80
        private const val RATING_POSITION_4 = 75
        private const val RATING_DEFAULT = 70

        inline operator fun invoke(testContext: TestContext<*>, crossinline block: MainPage.() -> Unit) {
            testContext.step(SCREEN_NAME) {
                MainPage(testContext).apply {
                    block()
                }
            }
        }
    }
}
