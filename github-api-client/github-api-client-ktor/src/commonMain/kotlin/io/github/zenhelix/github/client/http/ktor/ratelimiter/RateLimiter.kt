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
                    if ((data.remaining ?: Int.MAX_VALUE) <= config.remainingThreshold && resetTime > now) {
                        val waitTime = resetTime - now
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
    val rateLimitExceededTrigger: HttpResponse.() -> Boolean
)