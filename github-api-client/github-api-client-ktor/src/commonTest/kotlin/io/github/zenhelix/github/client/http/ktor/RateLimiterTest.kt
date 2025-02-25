package io.github.zenhelix.github.client.http.ktor

import io.github.zenhelix.github.client.http.ktor.ratelimiter.RateLimiterName
import io.github.zenhelix.github.client.http.ktor.ratelimiter.RateLimiting
import io.github.zenhelix.github.client.http.ktor.ratelimiter.requestWithRateLimiter
import io.github.zenhelix.github.client.http.ktor.ratelimiter.withRateLimiter
import io.github.zenhelix.github.client.http.ktor.utils.RateLimitLimit
import io.github.zenhelix.github.client.http.ktor.utils.RateLimitRemaining
import io.github.zenhelix.github.client.http.ktor.utils.RateLimitReset
import io.github.zenhelix.github.client.http.ktor.utils.RateLimitResource
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import test.clock
import test.createMockEngine
import test.mockEngine
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class RateLimiterTest {

    @Test
    fun `basic rate limiting functionality`() = runTest {
        val resetSeconds = 5.seconds

        val mockEngine = createMockEngine {
            addHandler {
                respond(
                    content = "{}",
                    headers = headersOf(
                        HttpHeaders.RateLimitLimit to listOf("60"),
                        HttpHeaders.RateLimitRemaining to listOf("0"),
                        HttpHeaders.RateLimitReset to listOf((clock().now() + resetSeconds).epochSeconds.toString()),
                        HttpHeaders.RateLimitResource to listOf("core")
                    )
                )
            }
            addHandler {
                respond(
                    content = "{}",
                    headers = headersOf(
                        HttpHeaders.RateLimitLimit to listOf("60"),
                        HttpHeaders.RateLimitRemaining to listOf("10"),
                        HttpHeaders.RateLimitReset to listOf((clock().now() + resetSeconds).epochSeconds.toString()),
                        HttpHeaders.RateLimitResource to listOf("core")
                    )
                )
            }

        }

        val client = HttpClient(mockEngine) {
            defaultRequest { withRateLimiter() }
            install(RateLimiting) {
                global(clock = clock())
            }
        }

        val startTime = clock().now()
        client.get("https://api.example.com")
        runCurrent()

        client.get("https://api.example.com")

        val elapsedSeconds = clock().now() - startTime
        assertTrue(
            elapsedSeconds >= resetSeconds,
            "Должен ждать сброса ограничения rate limit. Прошло: $elapsedSeconds, ожидалось >= $resetSeconds"
        )
    }

    @Test
    fun `rate limiter with custom config`() = runTest {
        val resetSeconds = 10L
        var requestCount = 0

        val mockEngine = mockEngine {
            requestCount++
            val isLimitedRequest = it.url.toString().contains("limit") || requestCount > 1

            val remaining = if (isLimitedRequest) "5" else "20"
            val reset = (clock().now().epochSeconds + resetSeconds).toString()

            respond(
                content = ByteReadChannel("{}"),
                status = if (isLimitedRequest && requestCount == 1) HttpStatusCode.TooManyRequests else HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.RateLimitLimit to listOf("60"),
                    HttpHeaders.RateLimitRemaining to listOf(remaining),
                    HttpHeaders.RateLimitReset to listOf(reset),
                    HttpHeaders.RateLimitResource to listOf("core")
                )
            )
        }

        val client = HttpClient(mockEngine) {
            defaultRequest { withRateLimiter() }
            install(RateLimiting) {
                global(clock = clock()) {
                    remainingThreshold = 10 // Приостанавливаться, когда оставшиеся запросы <= 10
                }
            }
        }

        // Первый запрос - нормальный
        client.get("https://api.example.com")
        runCurrent()

        // Второй запрос должен ждать, потому что remaining <= порога
        val startTime = clock().now().epochSeconds
        client.get("https://api.example.com/limit")

        // Время должно было продвинуться вперед на время сброса
        val elapsedSeconds = clock().now().epochSeconds - startTime
        assertTrue(
            elapsedSeconds >= resetSeconds,
            "Должен ждать сброса ограничения rate limit. Прошло: $elapsedSeconds, ожидалось >= $resetSeconds"
        )
    }

    @Test
    fun `multiple rate limiters for different resources`() = runTest {
        val resetSeconds = 5L

        val api1Name = RateLimiterName("api1")
        val api2Name = RateLimiterName("api2")

        val mockEngine = mockEngine { request ->
            val resource = if (request.url.toString().contains("api1")) "api1" else "api2"
            val remaining = if (resource == "api1") "0" else "10"
            val reset = (clock().now().epochSeconds + (if (resource == "api1") resetSeconds else 0)).toString()

            respond(
                content = ByteReadChannel("{}"),
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.RateLimitLimit to listOf("60"),
                    HttpHeaders.RateLimitRemaining to listOf(remaining),
                    HttpHeaders.RateLimitReset to listOf(reset),
                    HttpHeaders.RateLimitResource to listOf(resource)
                )
            )
        }

        val client = HttpClient(mockEngine) {
            defaultRequest { withRateLimiter() }
            install(RateLimiting) {
                rateLimiter(api1Name, clock = clock())
                rateLimiter(api2Name, clock = clock())
            }
        }

        // Первый запрос - api1 (с ограничением)
        client.requestWithRateLimiter(api1Name) {
            url("https://api.example.com/api1")
        }
        runCurrent()

        val startTime = clock().now().epochSeconds

        // Второй запрос к api1 должен ждать
        client.requestWithRateLimiter(api1Name) {
            url("https://api.example.com/api1")
        }

        // Должен был подождать сброса ограничения rate limit (5 секунд)
        val elapsedAfterApi1 = clock().now().epochSeconds - startTime
        assertTrue(
            elapsedAfterApi1 >= resetSeconds,
            "Должен ждать сброса ограничения rate limit для api1. Прошло: $elapsedAfterApi1, ожидалось >= $resetSeconds"
        )

        val timeBeforeApi2 = clock().now().epochSeconds

        // Запрос к api2 не должен ждать (есть доступные запросы)
        client.requestWithRateLimiter(api2Name) {
            url("https://api.example.com/api2")
        }

        // Для api2 не должно быть задержки
        val elapsedForApi2 = clock().now().epochSeconds - timeBeforeApi2
        assertTrue(
            elapsedForApi2 < resetSeconds,
            "Не должен ждать для api2. Прошло: $elapsedForApi2, должно быть < $resetSeconds"
        )
    }

    @Test
    fun `rate limiter handles 429 status correctly`() = runTest {
        val resetSeconds = 5L
        var requestCount = 0

        val mockEngine = mockEngine {
            requestCount++

            if (requestCount == 1) {
                // Первый запрос возвращает 429 Too Many Requests
                respond(
                    content = ByteReadChannel("Rate limit exceeded"),
                    status = HttpStatusCode.TooManyRequests,
                    headers = headersOf(
                        HttpHeaders.RateLimitLimit to listOf("60"),
                        HttpHeaders.RateLimitRemaining to listOf("0"),
                        HttpHeaders.RateLimitReset to listOf((clock().now().epochSeconds + resetSeconds).toString()),
                        HttpHeaders.RateLimitResource to listOf("core")
                    )
                )
            } else {
                // Последующие запросы возвращают OK
                respond(
                    content = ByteReadChannel("Success"),
                    status = HttpStatusCode.OK,
                    headers = headersOf(
                        HttpHeaders.RateLimitLimit to listOf("60"),
                        HttpHeaders.RateLimitRemaining to listOf("59"),
                        HttpHeaders.RateLimitReset to listOf((clock().now().epochSeconds + 3600).toString()),
                        HttpHeaders.RateLimitResource to listOf("core")
                    )
                )
            }
        }

        val client = HttpClient(mockEngine) {
            defaultRequest { withRateLimiter() }
            install(RateLimiting) {
                global(clock = clock())
            }
        }

        // Первый запрос - должен получить 429
        client.get("https://api.example.com")
        runCurrent()

        // Второй запрос - должен ждать из-за ограничения
        val startTime = clock().now().epochSeconds
        val response = client.get("https://api.example.com")

        // Должен был подождать сброса ограничения
        val elapsed = clock().now().epochSeconds - startTime
        assertTrue(
            elapsed >= resetSeconds,
            "Должен ждать сброса ограничения rate limit после 429. Прошло: $elapsed, ожидалось >= $resetSeconds"
        )
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `rate limiter respects manual time advancement`() = runTest {
        val resetSeconds = 5L

        val mockEngine = mockEngine {
            respond(
                content = ByteReadChannel("{}"),
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.RateLimitLimit to listOf("60"),
                    HttpHeaders.RateLimitRemaining to listOf("0"),
                    HttpHeaders.RateLimitReset to listOf((clock().now().epochSeconds + resetSeconds).toString()),
                    HttpHeaders.RateLimitResource to listOf("core")
                )
            )
        }

        val client = HttpClient(mockEngine) {
            defaultRequest { withRateLimiter() }
            install(RateLimiting) {
                global(clock = clock())
            }
        }

        // Первый запрос устанавливает данные о rate limit
        client.get("https://api.example.com")
        runCurrent()

        // Запускаем второй запрос, который должен быть заблокирован ограничением
        var secondRequestCompleted = false

        // Начинаем второй запрос, но не ждем его завершения
        val job = async {
            client.get("https://api.example.com")
            secondRequestCompleted = true
        }

        // Проверяем, что запрос еще не завершен
        runCurrent()
        assertFalse(secondRequestCompleted, "Запрос не должен завершиться до продвижения времени")

        // Продвигаем время вперед чуть больше, чем период сброса
        advanceTimeBy((resetSeconds * 1000) + 100)
        runCurrent()

        // Теперь запрос должен завершиться
        assertTrue(secondRequestCompleted, "Запрос должен завершиться после продвижения времени")
    }
}