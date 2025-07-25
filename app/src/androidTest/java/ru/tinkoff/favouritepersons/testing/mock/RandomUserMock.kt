package ru.tinkoff.favouritepersons.testing.mock

import com.github.tomakehurst.wiremock.client.WireMock.*

/**
 * Мок для API получения случайноого пользователя
 */
class RandomUserMock : Mock() {

    override val matcher = get(urlPathEqualTo("/api/"))
}
