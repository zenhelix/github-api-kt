package io.github.zenhelix.github.client.http.ktor.circuitbreaker

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest.DefaultRequestBuilder
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.util.collections.ConcurrentMap
import kotlinx.datetime.Clock
import kotlinx.datetime.Clock.System

/**
 * Register a [CircuitBreaker] with a given name
 */
@CircuitBreakerDsl
public fun CircuitBreakerConfig.register(
    name: CircuitBreakerName,
    clock: Clock = System,
    config: CircuitBreakerConfig.CircuitBreakerBuilder.() -> Unit
) {
    circuitBreakers.addCircuitBreaker(name, clock, config)
}

/**
 * Registers a global [CircuitBreaker] that is applied to the whole client
 */
@CircuitBreakerDsl
public fun CircuitBreakerConfig.global(
    clock: Clock = System,
    config: CircuitBreakerConfig.CircuitBreakerBuilder.() -> Unit
) {
    global = CircuitBreaker(
        name = CIRCUIT_BREAKER_NAME_GLOBAL,
        clock = clock,
        config = CircuitBreakerConfig.CircuitBreakerBuilder().apply(config)
    )
}

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

/**
 * Adds a [CircuitBreaker] to a [ConcurrentMap]
 */
internal fun ConcurrentMap<CircuitBreakerName, CircuitBreaker>.addCircuitBreaker(
    name: CircuitBreakerName,
    clock: Clock = System,
    config: CircuitBreakerConfig.CircuitBreakerBuilder.() -> Unit
) {
    require(!containsKey(name)) { "Circuit Breaker with name $name is already registered" }
    put(name, CircuitBreaker(name, clock, CircuitBreakerConfig.CircuitBreakerBuilder().apply(config)))
}
