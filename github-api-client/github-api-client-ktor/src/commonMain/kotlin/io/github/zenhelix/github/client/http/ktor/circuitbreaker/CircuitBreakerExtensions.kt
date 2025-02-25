package io.github.zenhelix.github.client.http.ktor.circuitbreaker

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest.DefaultRequestBuilder
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse

/**
 * Apply the [CircuitBreaker] with the given [CircuitBreakerName] to the request or the global one if name is given
 */
public fun HttpRequestBuilder.withCircuitBreaker(name: CircuitBreakerName = CIRCUIT_BREAKER_NAME_GLOBAL) {
    setAttributes { put(CircuitBreakerNameKey, name) }
}

public fun DefaultRequestBuilder.withCircuitBreaker(name: CircuitBreakerName = CIRCUIT_BREAKER_NAME_GLOBAL) {
    setAttributes { put(CircuitBreakerNameKey, name) }
}

/**
 * Make a request with the [CircuitBreaker] with the given [CircuitBreakerName]
 */
public suspend fun HttpClient.requestWithCircuitBreaker(
    name: CircuitBreakerName = CIRCUIT_BREAKER_NAME_GLOBAL,
    block: HttpRequestBuilder.() -> Unit
): HttpResponse = request {
    withCircuitBreaker(name)
    block()
}
