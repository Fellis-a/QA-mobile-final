package ru.tinkoff.favouritepersons.testing.mock.response

/**
 * Фабрика ответов для API RandomUser - упрощенная версия с только нужными полями
 */
object RandomUserResponseFactory {

    fun successResponse() = """
        {
            "results": [
                {
                    "gender": "male",
                    "name": {
                        "first": "Иван",
                        "last": "Иванов"
                    },
                    "location": {
                        "street": {
                            "number": 123,
                            "name": "ул. Тестовая"
                        },
                        "city": "Москва",
                        "state": "Московская область",
                        "country": "Россия"
                    },
                    "email": "ivan.ivanov@test.com",
                    "dob": {
                        "date": "1990-05-15T10:30:00.000Z",
                        "age": 33
                    },
                    "phone": "+7 900 123 45 67",
                    "picture": {
                        "medium": "https://randomuser.me/api/portraits/med/men/75.jpg"
                    },
                    "rating": 75
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

    fun femaleResponse() = """
        {
            "results": [
                {
                    "gender": "female",
                    "name": {
                        "first": "Анна",
                        "last": "Петрова"
                    },
                    "location": {
                        "street": {
                            "number": 456,
                            "name": "ул. Центральная"
                        },
                        "city": "Санкт-Петербург",
                        "state": "Ленинградская область",
                        "country": "Россия"
                    },
                    "email": "anna.petrova@test.com",
                    "dob": {
                        "date": "1985-12-25T15:45:00.000Z",
                        "age": 38
                    },
                    "phone": "+7 911 987 65 43",
                    "picture": {
                        "medium": "https://randomuser.me/api/portraits/med/women/85.jpg"
                    },
                    "rating": 85
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

    fun errorResponse() = """
        {
            "error": "Internal server error"
        }
    """.trimIndent()

    fun emptyResponse() = """
        {
            "results": [],
            "info": {
                "seed": "test",
                "results": 0,
                "page": 1,
                "version": "1.4"
            }
        }
    """.trimIndent()

    fun youngMaleResponse() = """
        {
            "results": [
                {
                    "gender": "male",
                    "name": {
                        "first": "Алексей",
                        "last": "Смирнов"
                    },
                    "location": {
                        "street": {
                            "number": 10,
                            "name": "ул. Молодежная"
                        },
                        "city": "Екатеринбург",
                        "state": "Свердловская область",
                        "country": "Россия"
                    },
                    "email": "alex.smirnov@example.com",
                    "dob": {
                        "date": "2001-03-10T08:15:00.000Z",
                        "age": 23
                    },
                    "phone": "+7 343 555 01 23",
                    "picture": {
                        "medium": "https://randomuser.me/api/portraits/med/men/22.jpg"
                    }
                }
            ],
            "info": {
                "seed": "young",
                "results": 1,
                "page": 1,
                "version": "1.4"
            }
        }
    """.trimIndent()

    fun elderlyFemaleResponse() = """
        {
            "results": [
                {
                    "gender": "female",
                    "name": {
                        "first": "Галина",
                        "last": "Сидорова"
                    },
                    "location": {
                        "street": {
                            "number": 67,
                            "name": "ул. Ветеранов"
                        },
                        "city": "Новосибирск",
                        "state": "Новосибирская область",
                        "country": "Россия"
                    },
                    "email": "galina.sidorova@mail.ru",
                    "dob": {
                        "date": "1952-08-20T12:00:00.000Z",
                        "age": 72
                    },
                    "phone": "+7 383 777 88 99",
                    "picture": {
                        "medium": "https://randomuser.me/api/portraits/med/women/60.jpg"
                    }
                }
            ],
            "info": {
                "seed": "elderly",
                "results": 1,
                "page": 1,
                "version": "1.4"
            }
        }
    """.trimIndent()

    fun foreignerResponse() = """
        {
            "results": [
                {
                    "gender": "male",
                    "name": {
                        "first": "John",
                        "last": "Smith"
                    },
                    "location": {
                        "street": {
                            "number": 42,
                            "name": "Main Street"
                        },
                        "city": "New York",
                        "state": "NY",
                        "country": "USA"
                    },
                    "email": "john.smith@gmail.com",
                    "dob": {
                        "date": "1988-11-03T14:22:00.000Z",
                        "age": 36
                    },
                    "phone": "+1 555 123 4567",
                    "picture": {
                        "medium": "https://randomuser.me/api/portraits/med/men/45.jpg"
                    },
                    "rating": 92
                }
            ],
            "info": {
                "seed": "foreigner",
                "results": 1,
                "page": 1,
                "version": "1.4"
            }
        }
    """.trimIndent()


    fun multipleStudentsForSortingResponse() = """
        {
            "results": [
                {
                    "gender": "male",
                    "name": {
                        "first": "Борис",
                        "last": "Антонов"
                    },
                    "location": {
                        "street": {
                            "number": 15,
                            "name": "ул. Первая"
                        },
                        "city": "Москва",
                        "state": "Московская область",
                        "country": "Россия"
                    },
                    "email": "boris.antonov@test.com",
                    "dob": {
                        "date": "1995-01-15T10:30:00.000Z",
                        "age": 30
                    },
                    "phone": "+7 900 111 11 11",
                    "picture": {
                        "medium": "https://randomuser.me/api/portraits/med/men/1.jpg"
                    },
                    "rating": 78
                },
                {
                    "gender": "female",
                    "name": {
                        "first": "Анна",
                        "last": "Белова"
                    },
                    "location": {
                        "street": {
                            "number": 25,
                            "name": "ул. Вторая"
                        },
                        "city": "СПб",
                        "state": "Ленинградская область",
                        "country": "Россия"
                    },
                    "email": "anna.belova@test.com",
                    "dob": {
                        "date": "1998-03-20T12:15:00.000Z",
                        "age": 26
                    },
                    "phone": "+7 911 222 22 22",
                    "picture": {
                        "medium": "https://randomuser.me/api/portraits/med/women/2.jpg"
                    },
                    "rating": 95
                },
                {
                    "gender": "male",
                    "name": {
                        "first": "Владимир",
                        "last": "Васильев"
                    },
                    "location": {
                        "street": {
                            "number": 35,
                            "name": "ул. Третья"
                        },
                        "city": "Казань",
                        "state": "Татарстан",
                        "country": "Россия"
                    },
                    "email": "vladimir.vasiliev@test.com",
                    "dob": {
                        "date": "1992-07-10T15:45:00.000Z",
                        "age": 32
                    },
                    "phone": "+7 843 333 33 33",
                    "picture": {
                        "medium": "https://randomuser.me/api/portraits/med/men/3.jpg"
                    },
                    "rating": 82
                },
                {
                    "gender": "female",
                    "name": {
                        "first": "Дарья",
                        "last": "Григорьева"
                    },
                    "location": {
                        "street": {
                            "number": 45,
                            "name": "ул. Четвертая"
                        },
                        "city": "Екатеринбург",
                        "state": "Свердловская область",
                        "country": "Россия"
                    },
                    "email": "darya.grigorieva@test.com",
                    "dob": {
                        "date": "1999-11-05T09:20:00.000Z",
                        "age": 25
                    },
                    "phone": "+7 343 444 44 44",
                    "picture": {
                        "medium": "https://randomuser.me/api/portraits/med/women/4.jpg"
                    },
                    "rating": 88
                },
                {
                    "gender": "male",
                    "name": {
                        "first": "Евгений",
                        "last": "Дмитриев"
                    },
                    "location": {
                        "street": {
                            "number": 55,
                            "name": "ул. Пятая"
                        },
                        "city": "Новосибирск",
                        "state": "Новосибирская область",
                        "country": "Россия"
                    },
                    "email": "evgeny.dmitriev@test.com",
                    "dob": {
                        "date": "1994-09-18T16:30:00.000Z",
                        "age": 30
                    },
                    "phone": "+7 383 555 55 55",
                    "picture": {
                        "medium": "https://randomuser.me/api/portraits/med/men/5.jpg"
                    },
                    "rating": 90
                }
            ],
            "info": {
                "seed": "sorting_test",
                "results": 5,
                "page": 1,
                "version": "1.4"
            }
        }
    """.trimIndent()


    fun largeStudentSetResponse() = """
        {
            "results": [
                {
                    "gender": "female",
                    "name": {
                        "first": "Александра",
                        "last": "Алексеева"
                    },
                    "email": "alexandra.alekseeva@test.com",
                    "dob": {
                        "date": "1997-02-14T11:00:00.000Z",
                        "age": 27
                    },
                    "rating": 75
                },
                {
                    "gender": "male",
                    "name": {
                        "first": "Николай",
                        "last": "Николаев"
                    },
                    "email": "nikolay.nikolaev@test.com",
                    "dob": {
                        "date": "1993-06-25T14:30:00.000Z",
                        "age": 31
                    },
                    "rating": 85
                },
                {
                    "gender": "female",
                    "name": {
                        "first": "Ольга",
                        "last": "Орлова"
                    },
                    "email": "olga.orlova@test.com",
                    "dob": {
                        "date": "1996-12-03T08:45:00.000Z",
                        "age": 28
                    },
                    "rating": 92
                },
                {
                    "gender": "male",
                    "name": {
                        "first": "Павел",
                        "last": "Павлов"
                    },
                    "email": "pavel.pavlov@test.com",
                    "dob": {
                        "date": "1991-04-17T13:15:00.000Z",
                        "age": 33
                    },
                    "rating": 88
                }
            ],
            "info": {
                "seed": "large_set",
                "results": 4,
                "page": 1,
                "version": "1.4"
            }
        }
    """.trimIndent()

    fun jittieTopsResponse() = """
        {
            "results": [
                {
                    "gender": "female",
                    "name": {
                        "first": "Jittie",
                        "last": "Tops"
                    },
                    "location": {
                        "street": {
                            "number": 789,
                            "name": "ул. Студенческая"
                        },
                        "city": "Москва",
                        "state": "Московская область",
                        "country": "Россия"
                    },
                    "email": "jittie.tops@university.com",
                    "dob": {
                        "date": "1999-06-12T09:30:00.000Z",
                        "age": 25
                    },
                    "phone": "+7 495 123 45 67",
                    "picture": {
                        "medium": "https://randomuser.me/api/portraits/med/women/42.jpg"
                    },
                    "rating": 92
                }
            ],
            "info": {
                "seed": "jittie",
                "results": 1,
                "page": 1,
                "version": "1.4"
            }
        }
    """.trimIndent()
}
