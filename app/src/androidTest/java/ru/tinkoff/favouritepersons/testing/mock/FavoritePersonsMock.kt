package ru.tinkoff.favouritepersons.testing.mock

/**
 * Основной класс для настройки моков API
 */
object FavoritePersonsMock {

    val randomUser = RandomUserMock()

    val favoritePersonsScenario = MockScenario()

    inline fun favoritePersonsMock(crossinline block: FavoritePersonsMock.() -> Unit) {
        block(this@FavoritePersonsMock)
    }
}
