package io.github.zenhelix.github.client.http.ktor.ratelimiter

import io.github.zenhelix.github.client.http.ktor.utils.RateLimitLimit
import io.github.zenhelix.github.client.http.ktor.utils.RateLimitRemaining
import io.github.zenhelix.github.client.http.ktor.utils.RateLimitReset
import io.github.zenhelix.github.client.http.ktor.utils.RateLimitResource
import io.github.zenhelix.github.client.http.ktor.utils.RateLimitUsed
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.datetime.Instant

internal data class RateLimitData(
    /**
     * The maximum number of requests that you can make per hour
     */
    val limit: Int?,
    /**
     * The number of requests remaining in the current rate limit window
     */
    val remaining: Int?,
    /**
     * The time at which the current rate limit window resets, in UTC epoch seconds
     */
    val reset: Instant?,
    /**
     * 	The number of requests you have made in the current rate limit window
     */
    val used: Int?,
    val resource: String?
) {

    fun fromHttpHeaders(headers: Headers): RateLimitData = RateLimitData(
        limit = headers[HttpHeaders.RateLimitLimit]?.toIntOrNull(),
        remaining = headers[HttpHeaders.RateLimitRemaining]?.toIntOrNull(),
        reset = headers[HttpHeaders.RateLimitReset]?.toLongOrNull()?.let { Instant.fromEpochMilliseconds(it) },
        used = headers[HttpHeaders.RateLimitUsed]?.toIntOrNull(),
        resource = headers[HttpHeaders.RateLimitResource]
    )

}
