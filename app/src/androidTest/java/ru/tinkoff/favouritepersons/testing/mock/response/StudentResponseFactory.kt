package ru.tinkoff.favouritepersons.testing.mock.response

/**
 * Фабрика для создания JSON ответов для тестирования
 * Содержит методы для генерации различных типов студентов
 */
object StudentResponseFactory {

    /**
     * Создает стандартный ответ для студента
     */
    fun createStudentResponse(firstName: String, lastName: String, rating: Int) = """
        {
            "results": [
                {
                    "gender": "${if (firstName.endsWith("а")) "female" else "male"}",
                    "name": {
                        "first": "$firstName",
                        "last": "$lastName"
                    },
                    "location": {
                        "street": {
                            "number": 123,
                            "name": "ул. Тестовая"
                        },
                        "city": "Москва",
                        "state": "Московская область",
                        "country": "Россия",
                        "postcode": "123456",
                        "coordinates": {
                            "latitude": "55.7558",
                            "longitude": "37.6176"
                        },
                        "timezone": {
                            "offset": "+3:00",
                            "description": "Moscow"
                        }
                    },
                    "email": "${firstName.lowercase()}.${lastName.lowercase()}@test.com",
                    "login": {
                        "uuid": "12345",
                        "username": "${firstName.lowercase()}",
                        "password": "password",
                        "salt": "salt",
                        "md5": "md5",
                        "sha1": "sha1",
                        "sha256": "sha256"
                    },
                    "dob": {
                        "date": "1995-05-15T10:30:00.000Z",
                        "age": 29
                    },
                    "registered": {
                        "date": "2020-01-01T00:00:00.000Z",
                        "age": 5
                    },
                    "phone": "+7 900 123 45 67",
                    "cell": "+7 900 123 45 67",
                    "id": {
                        "name": "test",
                        "value": "123456"
                    },
                    "picture": {
                        "large": "https://randomuser.me/api/portraits/men/75.jpg",
                        "medium": "https://randomuser.me/api/portraits/med/men/75.jpg",
                        "thumbnail": "https://randomuser.me/api/portraits/thumb/men/75.jpg"
                    },
                    "nat": "RU",
                    "rating": $rating
                }
            ],
            "info": {
                "seed": "test",
                "results": 1,
                "page": 1,
                "version": "1.4"
            }
        }
    """.trimIndent()

    /**
     * Создает ответ для студента с определенным возрастом
     */
    fun createStudentResponseWithAge(firstName: String, lastName: String, rating: Int, age: Int) = """
        {
            "results": [
                {
                    "gender": "${if (firstName.endsWith("а")) "female" else "male"}",
                    "name": {
                        "first": "$firstName",
                        "last": "$lastName"
                    },
                    "location": {
                        "street": { "number": 123, "name": "ул. Тестовая" },
                        "city": "Москва",
                        "state": "Московская область",
                        "country": "Россия",
                        "postcode": "123456",
                        "coordinates": { "latitude": "55.7558", "longitude": "37.6176" },
                        "timezone": { "offset": "+3:00", "description": "Moscow" }
                    },
                    "email": "${firstName.lowercase()}.${lastName.lowercase()}@test.com",
                    "login": {
                        "uuid": "12345", "username": "${firstName.lowercase()}", "password": "password",
                        "salt": "salt", "md5": "md5", "sha1": "sha1", "sha256": "sha256"
                    },
                    "dob": {
                        "date": "${2025 - age}-05-15T10:30:00.000Z",
                        "age": $age
                    },
                    "registered": { "date": "2020-01-01T00:00:00.000Z", "age": 5 },
                    "phone": "+7 900 123 45 67",
                    "cell": "+7 900 123 45 67",
                    "id": { "name": "test", "value": "123456" },
                    "picture": {
                        "large": "https://randomuser.me/api/portraits/men/75.jpg",
                        "medium": "https://randomuser.me/api/portraits/med/men/75.jpg",
                        "thumbnail": "https://randomuser.me/api/portraits/thumb/men/75.jpg"
                    },
                    "nat": "RU",
                    "rating": $rating
                }
            ],
            "info": { "seed": "test", "results": 1, "page": 1, "version": "1.4" }
        }
    """.trimIndent()

    /**
     * Создает список готовых студентов для массового тестирования
     */
    fun getTestStudentsData() = listOf(
        Triple("Анна", "Белова", 95),
        Triple("Борис", "Антонов", 78),
        Triple("Владимир", "Васильев", 82),
        Triple("Дарья", "Григорьева", 88),
        Triple("Евгений", "Дмитриев", 90),
        Triple("Жанна", "Жукова", 77),
        Triple("Игорь", "Иванов", 85),
        Triple("Ксения", "Козлова", 93),
        Triple("Лев", "Львов", 80),
        Triple("Мария", "Морозова", 91),
        Triple("Николай", "Николаев", 84),
        Triple("Ольга", "Орлова", 89)
    )

    /**
     * Создает список студентов для тестирования сортировки по именам
     */
    fun getSortByNameTestData() = listOf(
        Triple("Михаил", "Сидоров", 85),
        Triple("Анна", "Белова", 90),
        Triple("Дарья", "Григорьева", 88),
        Triple("Борис", "Антонов", 82)
    )

    /**
     * Создает список студентов для тестирования сортировки по рейтингу
     */
    fun getSortByRatingTestData() = listOf(
        Triple("Студент", "Первый", 75),
        Triple("Студент", "Второй", 95),
        Triple("Студент", "Третий", 85),
        Triple("Студент", "Четвертый", 65),
        Triple("Студент", "Пятый", 90)
    )
}
