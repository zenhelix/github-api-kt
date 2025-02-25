package io.github.zenhelix.github.client.http.ktor.ratelimiter

import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.Clock.System
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal class RateLimiter(
    private val name: RateLimiterName,
    private val clock: Clock = System,
    private val config: RateLimitConfiguration
) {
    private val rateLimits = mutableMapOf<String, RateLimitData>()
    private val mutex = Mutex()

    suspend fun waitIfNeeded() {
        mutex.withLock {
            val now = clock.now()
            var delayNeeded = 0.seconds

            rateLimits.values.forEach { data ->
                data.reset?.let { resetTime ->
                    // Add a small margin to ensure we're truly past the reset time
                    val remainingCount = data.remaining ?: Int.MAX_VALUE
                    if (remainingCount <= config.remainingThreshold && resetTime > now) {
                        val waitTime = resetTime - now + config.safetyMargin
                        if (waitTime > delayNeeded) {
                            delayNeeded = waitTime
                        }
                    }
                }
            }

            if (delayNeeded.inWholeSeconds > 0) {
                delay(delayNeeded)
            }
        }
    }

    suspend fun handleResponse(response: HttpResponse) {
        val data = fromHttpHeaders(response.headers)
        val resource = data.resource ?: name.value

        mutex.withLock {
            if (config.rateLimitExceededTrigger(response)) {
                rateLimits[resource] = data.copy(
                    remaining = 0,
                    reset = data.reset ?: (clock.now() + config.defaultResetDelay)
                )
            } else if (data.limit != null || data.remaining != null || data.reset != null) {
                // Update only if the response contains information about limits
                val existingData = rateLimits[resource]
                if (existingData != null) {
                    rateLimits[resource] = existingData.copy(
                        limit = data.limit ?: existingData.limit,
                        remaining = data.remaining ?: existingData.remaining,
                        reset = data.reset ?: existingData.reset,
                        used = data.used ?: existingData.used
                    )
                } else {
                    rateLimits[resource] = data
                }
            }
        }
    }

    private fun fromHttpHeaders(headers: Headers): RateLimitData = RateLimitData(
        limit = headers[config.limitHeader]?.toIntOrNull(),
        remaining = headers[config.remainingHeader]?.toIntOrNull(),
        reset = headers[config.resetHeader]?.toLongOrNull()?.let { Instant.fromEpochSeconds(it) },
        used = headers[config.usedHeader]?.toIntOrNull(),
        resource = headers[config.resourceHeader]
    )
}

internal data class RateLimitConfiguration(
    val limitHeader: String,
    val remainingHeader: String,
    val resetHeader: String,
    val usedHeader: String,
    val resourceHeader: String,
    val remainingThreshold: Int,
    val defaultResetDelay: Duration,
    val safetyMargin: Duration,
    val rateLimitExceededTrigger: HttpResponse.() -> Boolean
)