package io.github.zenhelix.github.client.http.ktor.ratelimiter

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
)