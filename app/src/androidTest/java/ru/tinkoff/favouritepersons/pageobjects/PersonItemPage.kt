package ru.tinkoff.favouritepersons.pageobjects

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.edit.KTextInputLayout
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import ru.tinkoff.favouritepersons.R
import ru.tinkoff.favouritepersons.presentation.activities.PersonItemActivity

/**
 * PageObject для экрана добавления/редактирования студента
 */
class PersonItemPage(testContext: TestContext<*>) : BaseScreen(testContext) {

    override val layoutId: Int = R.layout.person_item_activity
    override val viewClass: Class<*> = PersonItemActivity::class.java

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    val screenTitle = KTextView { withId(R.id.tw_person_screen_title) }

    val nameInputLayout = KTextInputLayout { withId(R.id.til_name) }
    val nameInput = KEditText { withId(R.id.et_name) }

    val surnameInputLayout = KTextInputLayout { withId(R.id.til_surname) }
    val surnameInput = KEditText { withId(R.id.et_surname) }

    val genderInputLayout = KTextInputLayout { withId(R.id.til_gender) }
    val genderInput = KEditText { withId(R.id.et_gender) }

    val birthdateInputLayout = KTextInputLayout { withId(R.id.til_birthdate) }
    val birthdateInput = KEditText { withId(R.id.et_birthdate) }

    val emailInputLayout = KTextInputLayout { withId(R.id.til_email) }
    val emailInput = KEditText { withId(R.id.et_email) }

    val phoneInputLayout = KTextInputLayout { withId(R.id.til_phone) }
    val phoneInput = KEditText { withId(R.id.et_phone) }

    val addressInputLayout = KTextInputLayout { withId(R.id.til_address) }
    val addressInput = KEditText { withId(R.id.et_address) }

    val imageLinkInputLayout = KTextInputLayout { withId(R.id.til_image_link) }
    val imageLinkInput = KEditText { withId(R.id.et_image) }

    val scoreInputLayout = KTextInputLayout { withId(R.id.til_score) }
    val scoreInput = KEditText { withId(R.id.et_score) }

    val saveButton = KButton { withId(R.id.submit_button) }

    fun fillPersonData(
        name: String,
        surname: String,
        gender: String = "",
        birthdate: String = "",
        email: String = "",
        phone: String = "",
        address: String = "",
        imageLink: String = "",
        score: String = ""
    ) {
        step("Заполняем данные персоны: $name $surname") {
            nameInput.replaceText(name)
            surnameInput.replaceText(surname)

            if (gender.isNotEmpty()) {
                genderInput.replaceText(gender)
            }

            if (birthdate.isNotEmpty()) {
                birthdateInput.replaceText(birthdate)
            }

            if (email.isNotEmpty()) {
                emailInput.replaceText(email)
            }

            if (phone.isNotEmpty()) {
                phoneInput.replaceText(phone)
            }

            if (address.isNotEmpty()) {
                addressInput.replaceText(address)
            }

            if (imageLink.isNotEmpty()) {
                imageLinkInput.replaceText(imageLink)
            }

            if (score.isNotEmpty()) {
                scoreInput.replaceText(score)
            }
        }
    }

    fun fillName(name: String) {
        step("Вводим имя: $name") {
            nameInput.replaceText(name)
        }
    }

    fun fillSurname(surname: String) {
        step("Вводим фамилию: $surname") {
            surnameInput.replaceText(surname)
        }
    }

    fun fillGender(gender: String) {
        step("Вводим пол: $gender") {
            genderInput.replaceText(gender)
        }
    }

    fun fillBirthdate(birthdate: String) {
        step("Вводим дату рождения: $birthdate") {
            birthdateInput.replaceText(birthdate)
        }
    }

    fun fillEmail(email: String) {
        step("Вводим email: $email") {
            emailInput.replaceText(email)
        }
    }

    fun fillPhone(phone: String) {
        step("Вводим телефон: $phone") {
            phoneInput.replaceText(phone)
        }
    }

    fun fillAddress(address: String) {
        step("Вводим адрес: $address") {
            addressInput.replaceText(address)
        }
    }

    fun fillImageLink(imageLink: String) {
        step("Вводим ссылку на изображение: $imageLink") {
            imageLinkInput.replaceText(imageLink)
        }
    }

    fun fillScore(score: String) {
        step("Вводим оценку: $score") {
            scoreInput.replaceText(score)
        }
    }

    fun clickSave() {
        step("Нажимаем кнопку Сохранить") {
            saveButton.click()
        }
    }

    fun checkSaveButtonDisabled() {
        step("Проверяем что кнопка Сохранить неактивна") {
            saveButton.isDisabled()
        }
    }

    fun isSaveButtonEnabled(): Boolean {
        return try {
            saveButton.isEnabled()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun minimizeApp() {
        step("Сворачиваем приложение") {
            device.pressHome()
        }
    }

    fun reopenApp() {
        step("Возвращаемся в приложение") {
            device.pressRecentApps()

            Thread.sleep(1000)

            device.click(device.displayWidth / 2, device.displayHeight / 2)
        }
    }

    fun isAddMode(): Boolean {
        return try {
            screenTitle.containsText("Добавление пользователя")
            true
        } catch (e: Exception) {
            try {
                screenTitle.containsText("Новая")
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    fun isEditMode(): Boolean {
        return try {
            screenTitle.containsText("Редактирование пользователя")
            true
        } catch (e: Exception) {
            try {
                screenTitle.containsText("Изменить")
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    fun checkScreenOpened() {
        step("Проверяем что экран персоны открылся") {
            nameInput.isDisplayed()
            surnameInput.isDisplayed()
            screenTitle.isDisplayed()
        }
    }

    fun checkFieldsEnabled() {
        step("Проверяем что поля ввода активны") {
            nameInput.isEnabled()
            surnameInput.isEnabled()
            emailInput.isEnabled()
            phoneInput.isEnabled()
        }
    }

    fun checkPersonData(
        name: String,
        surname: String,
        gender: String = "",
        birthdate: String = "",
        email: String = "",
        phone: String = "",
        address: String = "",
        imageLink: String = "",
        score: String = ""
    ) {
        step("Проверяем данные персоны: $name $surname") {
            nameInput.hasText(name)
            surnameInput.hasText(surname)

            if (gender.isNotEmpty()) genderInput.hasText(gender)
            if (birthdate.isNotEmpty()) birthdateInput.hasText(birthdate)
            if (email.isNotEmpty()) emailInput.hasText(email)
            if (phone.isNotEmpty()) phoneInput.hasText(phone)
            if (address.isNotEmpty()) addressInput.hasText(address)
            if (imageLink.isNotEmpty()) imageLinkInput.hasText(imageLink)
            if (score.isNotEmpty()) scoreInput.hasText(score)
        }
    }

    fun checkValidationError(fieldName: String) {
        step("Проверяем ошибку валидации для поля $fieldName") {
            when (fieldName.lowercase()) {
                "name" -> nameInputLayout.isDisplayed()
                "surname" -> surnameInputLayout.isDisplayed()
                "gender" -> genderInputLayout.isDisplayed()
                "birthdate" -> birthdateInputLayout.isDisplayed()
                "email" -> emailInputLayout.isDisplayed()
                "phone" -> phoneInputLayout.isDisplayed()
                "address" -> addressInputLayout.isDisplayed()
                "imagelink" -> imageLinkInputLayout.isDisplayed()
                "score" -> scoreInputLayout.isDisplayed()
            }
        }
    }

    fun checkValidationErrorMessage(fieldName: String, expectedMessage: String) {
        step("Проверяем сообщение ошибки '$expectedMessage' для поля $fieldName") {
            when (fieldName.lowercase()) {
                "name" -> nameInputLayout.hasError(expectedMessage)
                "surname" -> surnameInputLayout.hasError(expectedMessage)
                "gender" -> genderInputLayout.hasError(expectedMessage)
                "birthdate" -> birthdateInputLayout.hasError(expectedMessage)
                "email" -> emailInputLayout.hasError(expectedMessage)
                "phone" -> phoneInputLayout.hasError(expectedMessage)
                "address" -> addressInputLayout.hasError(expectedMessage)
                "imagelink" -> imageLinkInputLayout.hasError(expectedMessage)
                "score" -> scoreInputLayout.hasError(expectedMessage)
            }
        }
    }

    fun checkNoValidationErrors() {
        step("Проверяем отсутствие ошибок валидации для поля имени") {
            nameInputLayout.hasNoError()
        }
    }

    fun waitForScreenClosed() {
        step("Ожидаем закрытия экрана персоны") {
            Thread.sleep(2000)
        }
    }

    fun goBack() {
        step("Возвращаемся назад с экрана персоны") {
            device.pressBack()
        }
    }

    fun goBackSafely() {
        step("Безопасно возвращаемся назад с экрана персоны") {
            device.pressBack()
            waitForScreenClosed()
        }
    }

    companion object {
        const val SCREEN_NAME = "Экран персоны"

        inline operator fun invoke(testContext: TestContext<*>, crossinline block: PersonItemPage.() -> Unit) {
            testContext.step(SCREEN_NAME) {
                PersonItemPage(testContext).apply {
                    block()
                }
            }
        }
    }
}
