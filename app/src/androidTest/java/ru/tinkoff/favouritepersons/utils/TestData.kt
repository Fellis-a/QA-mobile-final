package ru.tinkoff.favouritepersons.utils

object TestData {

    data class PersonData(
        val name: String,
        val surname: String,
        val gender: String = "М",
        val birthdate: String = "1990-01-01",
        val email: String,
        val phone: String = "+1234567890",
        val address: String = "Test Address",
        val imageUrl: String = "https://example.com/image.jpg",
        val score: String = "5"
    )

    val validPersonData = PersonData(
        name = "John",
        surname = "Doe",
        email = "john.doe@example.com"
    )

    val invalidPersonData = PersonData(
        name = "",
        surname = "",
        email = "invalid-email"
    )

    val personWithHighScore = PersonData(
        name = "Alice",
        surname = "Johnson",
        email = "alice@test.com",
        score = "10"
    )

    val personWithLowScore = PersonData(
        name = "Bob",
        surname = "Smith",
        email = "bob@test.com",
        score = "1"
    )


}
