package io.github.zenhelix.github.client.http.ktor.ratelimiter

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest.DefaultRequestBuilder
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse

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
