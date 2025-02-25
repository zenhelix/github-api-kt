package io.github.zenhelix.github.client.http.ktor.ratelimiter

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest.DefaultRequestBuilder
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.util.collections.ConcurrentMap
import kotlinx.datetime.Clock
import kotlinx.datetime.Clock.System

/**
 * Register a [RateLimiter] with a given name
 */
@RateLimiterDsl
public fun RateLimiterConfig.register(
    name: RateLimiterName,
    clock: Clock = System,
    config: RateLimiterConfig.RateLimiterBuilder.() -> Unit
) {
    rateLimiters.addRateLimiter(name, clock, config)
}

/**
 * Registers a global [RateLimiter] that is applied to the whole client
 */
@RateLimiterDsl
public fun RateLimiterConfig.global(
    clock: Clock = System,
    config: RateLimiterConfig.RateLimiterBuilder.() -> Unit
) {
    global = RateLimiter(
        name = RATE_LIMITER_NAME_GLOBAL,
        clock = clock,
        config = RateLimiterConfig.RateLimiterBuilder().apply(config)
    )
}

/**
 * Apply the [RateLimiter] with the given [RateLimiterName] to the request or the global one if name is given
 */
public fun HttpRequestBuilder.withRateLimiter(name: RateLimiterName = RATE_LIMITER_NAME_GLOBAL) {
    setAttributes { put(RateLimiterNameKey, name) }
}

public fun DefaultRequestBuilder.withRateLimiter(name: RateLimiterName = RATE_LIMITER_NAME_GLOBAL) {
    setAttributes { put(RateLimiterNameKey, name) }
}

/**
 * Make a request with the [RateLimiter] with the given [RateLimiterName]
 */
public suspend fun HttpClient.requestWithRateLimiter(
    name: RateLimiterName = RATE_LIMITER_NAME_GLOBAL,
    block: HttpRequestBuilder.() -> Unit
): HttpResponse = request {
    withRateLimiter(name)
    block()
}

/**
 * Adds a [RateLimiter] to a [ConcurrentMap]
 */
internal fun ConcurrentMap<RateLimiterName, RateLimiter>.addRateLimiter(
    name: RateLimiterName,
    clock: Clock = System,
    config: RateLimiterConfig.RateLimiterBuilder.() -> Unit
) {
    require(!containsKey(name)) { "Rate limiter with name $name is already registered" }
    put(name, RateLimiter(name, clock, RateLimiterConfig.RateLimiterBuilder().apply(config)))
}
