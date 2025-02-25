package io.github.zenhelix.github.client.http.ktor.ratelimiter

import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.Clock.System
import kotlinx.datetime.Instant
import kotlin.properties.Delegates
import kotlin.time.Duration.Companion.seconds

internal class RateLimiter(
    private val name: RateLimiterName,
    private val clock: Clock = System,
    private val config: RateLimiterConfig.RateLimiterBuilder
) {
    private val rateLimits = mutableMapOf<String, RateLimitData>()
    private val mutex = Mutex()

    private var scope: CoroutineScope by Delegates.notNull()

    internal fun initialize(dispatcher: CoroutineDispatcher) {
        scope = CoroutineScope(dispatcher)
    }

    suspend fun waitIfNeeded() {
        mutex.withLock {
            val now = clock.now().toEpochMilliseconds()
            var maxResetTime = now

            rateLimits.values.forEach { data ->
                data.reset?.toEpochMilliseconds()?.let { resetTime ->
                    if ((data.remaining ?: 1) <= 0 && resetTime > now) {
                        if (resetTime > maxResetTime) {
                            maxResetTime = resetTime
                        }
                    }
                }
            }

            if (maxResetTime > now) {
                delay(maxResetTime - now)
            }
        }
    }

    suspend fun handleResponse(response: HttpResponse) {
        val data = fromHttpHeaders(response.headers)
        val resource = data.resource ?: return

        mutex.lock()
        try {
            if (config.rateLimitExceededTrigger(response)) {
                rateLimits[resource] = data.copy(
                    remaining = 0,
                    reset = data.reset ?: (clock.now() + 60.seconds)
                )
            } else {
                rateLimits[resource] = data
            }
        } finally {
            mutex.unlock()
        }
    }

    suspend fun getMaxResetTime(): Long = mutex.withLock {
        rateLimits.values.maxOfOrNull { it.reset?.toEpochMilliseconds() ?: 0 } ?: 0
    }

    private fun fromHttpHeaders(headers: Headers): RateLimitData = RateLimitData(
        limit = headers[config.limitHeader]?.toIntOrNull(),
        remaining = headers[config.remainingHeader]?.toIntOrNull(),
        reset = headers[config.resetHeader]?.toLongOrNull()?.let { Instant.fromEpochSeconds(it) },
        used = headers[config.usedHeader]?.toIntOrNull(),
        resource = headers[config.resourceHeader]
    )

}