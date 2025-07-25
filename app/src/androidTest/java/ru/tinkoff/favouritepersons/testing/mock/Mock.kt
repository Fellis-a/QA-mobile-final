package ru.tinkoff.favouritepersons.testing.mock

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.google.gson.Gson

/**
 * Базовый класс для всех моков API
 */
abstract class Mock {

    abstract val matcher: MappingBuilder

    inline fun <reified T> respondWith(response: T) {
        respondWith(200, response)
    }

    inline fun <reified T> respondWith(code: Int, response: T) {
        val body = if (response is String) response else Gson().toJson(response)
        val responseDefinition = WireMock.aResponse().withStatus(code).withBody(body)
        stubFor(matcher.willReturn(responseDefinition))
    }
}
