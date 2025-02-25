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
import kotlin.time.Duration.Companion.milliseconds

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
            val now = clock.now()
            var delayNeeded = 0L

            rateLimits.values.forEach { data ->
                data.reset?.let { resetTime ->
                    if ((data.remaining ?: Int.MAX_VALUE) <= config.remainingThreshold && resetTime > now) {
                        val waitTime = resetTime.toEpochMilliseconds() - now.toEpochMilliseconds()
                        if (waitTime > delayNeeded) {
                            delayNeeded = waitTime
                        }
                    }
                }
            }
            if (delayNeeded > 0) {
                delay(delayNeeded.milliseconds)
            }
        }
    }

    suspend fun handleResponse(response: HttpResponse) {
        val data = fromHttpHeaders(response.headers)
        // Если ресурс не указан, используем дефолтный ресурс на основе имени rate limiter
        val resource = data.resource ?: name.value

        mutex.withLock {
            if (config.rateLimitExceededTrigger(response)) {
                // Если сработал триггер превышения лимита
                rateLimits[resource] = data.copy(
                    remaining = 0,
                    reset = data.reset ?: (clock.now() + config.defaultResetDelay)
                )
            } else if (data.limit != null || data.remaining != null || data.reset != null) {
                // Обновляем только если в ответе есть информация о лимитах
                val existingData = rateLimits[resource]
                if (existingData != null) {
                    // Обновляем существующие данные только с новыми значениями
                    rateLimits[resource] = existingData.copy(
                        limit = data.limit ?: existingData.limit,
                        remaining = data.remaining ?: existingData.remaining,
                        reset = data.reset ?: existingData.reset,
                        used = data.used ?: existingData.used
                    )
                } else {
                    // Сохраняем новые данные
                    rateLimits[resource] = data
                }
            }
        }
    }

    suspend fun getMaxResetTime(): Long = mutex.withLock {
        rateLimits.values.maxOfOrNull { it.reset?.toEpochMilliseconds() ?: 0 } ?: 0
    }

    suspend fun getRateLimits(): Map<String, RateLimitData> = mutex.withLock {
        rateLimits.toMap()
    }

    private fun fromHttpHeaders(headers: Headers): RateLimitData = RateLimitData(
        limit = headers[config.limitHeader]?.toIntOrNull(),
        remaining = headers[config.remainingHeader]?.toIntOrNull(),
        reset = headers[config.resetHeader]?.toLongOrNull()?.let { Instant.fromEpochSeconds(it) },
        used = headers[config.usedHeader]?.toIntOrNull(),
        resource = headers[config.resourceHeader]
    )
}