package ru.tinkoff.favouritepersons.testing.mock

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.google.gson.Gson

/**
 * Класс для создания сложных сценариев тестирования с последовательными запросами
 */
class MockScenario {

    private inline infix fun <reified T> Mock.respondInScenarioWith(response: T) {
        this.respondInScenarioWith(200, response)
    }

    private inline fun <reified T> Mock.respondInScenarioWith(code: Int, response: T) {
        val body = if (response is String) response else Gson().toJson(response)
        val responseDefinition = aResponse().withStatus(code).withBody(body)
        WireMockScenarioFactory.addMock(this.matcher, responseDefinition)
    }


}
