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
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class RateLimiterTest {

    @Test
    fun `basic rate limiting functionality`() = runTest {
        val resetDelay = 5.seconds

        val mockEngine = createMockEngine {
            addHandler {
                respond(
                    content = "{}",
                    headers = headersOf(
                        HttpHeaders.RateLimitLimit to listOf("60"),
                        HttpHeaders.RateLimitRemaining to listOf("0"),
                        HttpHeaders.RateLimitReset to listOf((clock().now() + resetDelay).epochSeconds.toString()),
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
                        HttpHeaders.RateLimitReset to listOf((clock().now() + resetDelay).epochSeconds.toString()),
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
            elapsedSeconds >= resetDelay,
            "Should wait for rate limit reset. Elapsed: $elapsedSeconds, expected >= $resetDelay"
        )
    }

    @Test
    fun `rate limiter with custom config`() = runTest {
        val resetDelay = 10.seconds

        val mockEngine = createMockEngine {

            addHandler {
                respond(
                    content = "{}",
                    status = HttpStatusCode.OK,
                    headers = headersOf(
                        HttpHeaders.RateLimitLimit to listOf("60"),
                        HttpHeaders.RateLimitRemaining to listOf("5"),
                        HttpHeaders.RateLimitReset to listOf((clock().now() + resetDelay).epochSeconds.toString()),
                        HttpHeaders.RateLimitResource to listOf("core")
                    )
                )
            }

            addHandler {
                respond(
                    content = "{}",
                    status = HttpStatusCode.TooManyRequests,
                    headers = headersOf(
                        HttpHeaders.RateLimitLimit to listOf("60"),
                        HttpHeaders.RateLimitRemaining to listOf("20"),
                        HttpHeaders.RateLimitReset to listOf((clock().now() + resetDelay).epochSeconds.toString()),
                        HttpHeaders.RateLimitResource to listOf("core")
                    )
                )
            }
        }

        val client = HttpClient(mockEngine) {
            defaultRequest { withRateLimiter() }
            install(RateLimiting) {
                global(clock = clock()) {
                    remainingThreshold = 10 // Pause when remaining requests <= 10
                }
            }
        }

        // First request - normal
        client.get("https://api.example.com")
        runCurrent()

        // Second request should wait because remaining <= threshold
        val startTime = clock().now()
        client.get("https://api.example.com/limit")

        // Time should have advanced forward by reset time
        val elapsedSeconds = clock().now() - startTime
        assertTrue(
            elapsedSeconds >= resetDelay,
            "Should wait for rate limit reset. Elapsed: $elapsedSeconds, expected >= $resetDelay"
        )
    }

    @Test
    fun `multiple rate limiters for different resources`() = runTest {
        val resetDelay = 5.seconds

        val api1Name = RateLimiterName("api1")
        val api2Name = RateLimiterName("api2")

        val mockEngine = mockEngine { request ->
            val resource = if (request.url.toString().contains("api1")) {
                "api1"
            } else {
                "api2"
            }
            val remaining = if (resource == "api1") {
                0
            } else {
                10
            }
            val reset = (clock().now() + if (resource == "api1") {
                resetDelay
            } else {
                0.seconds
            })

            respond(
                content = "{}",
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.RateLimitLimit to listOf("60"),
                    HttpHeaders.RateLimitRemaining to listOf(remaining.toString()),
                    HttpHeaders.RateLimitReset to listOf(reset.epochSeconds.toString()),
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

        // First request - api1 (with limitation)
        client.requestWithRateLimiter(api1Name) {
            url("https://api.example.com/api1")
        }
        runCurrent()

        val startTime = clock().now()

        // Second request to api1 should wait
        client.requestWithRateLimiter(api1Name) {
            url("https://api.example.com/api1")
        }

        // Should have waited for rate limit reset (5 seconds)
        val elapsedAfterApi1 = clock().now() - startTime
        assertTrue(
            elapsedAfterApi1 >= resetDelay,
            "Should wait for rate limit reset for api1. Elapsed: $elapsedAfterApi1, expected >= $resetDelay"
        )

        val timeBeforeApi2 = clock().now()

        // Request to api2 should not wait (has available requests)
        client.requestWithRateLimiter(api2Name) {
            url("https://api.example.com/api2")
        }

        // For api2 there should be no delay
        val elapsedForApi2 = clock().now() - timeBeforeApi2
        assertTrue(
            elapsedForApi2 < resetDelay,
            "Should not wait for api2. Elapsed: $elapsedForApi2, should be < $resetDelay"
        )
    }

    @Test
    fun `rate limiter handles 429 status correctly`() = runTest {
        val resetDelay = 5.seconds

        val mockEngine = createMockEngine {
            addHandler {
                // First request returns 429 Too Many Requests
                respond(
                    content = ByteReadChannel("Rate limit exceeded"),
                    status = HttpStatusCode.TooManyRequests,
                    headers = headersOf(
                        HttpHeaders.RateLimitLimit to listOf("60"),
                        HttpHeaders.RateLimitRemaining to listOf("0"),
                        HttpHeaders.RateLimitReset to listOf((clock().now() + resetDelay).epochSeconds.toString()),
                        HttpHeaders.RateLimitResource to listOf("core")
                    )
                )
            }
            // Subsequent requests return OK
            addHandler {
                respond(
                    content = ByteReadChannel("Success"),
                    status = HttpStatusCode.OK,
                    headers = headersOf(
                        HttpHeaders.RateLimitLimit to listOf("60"),
                        HttpHeaders.RateLimitRemaining to listOf("59"),
                        HttpHeaders.RateLimitReset to listOf((clock().now() + 1.hours).epochSeconds.toString()),
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

        // First request - should get 429
        client.get("https://api.example.com")
        runCurrent()

        // Second request - should wait due to rate limit
        val startTime = clock().now()
        val response = client.get("https://api.example.com")

        // Should have waited for rate limit reset
        val elapsed = clock().now() - startTime
        assertTrue(
            elapsed >= resetDelay,
            "Should wait for rate limit reset after 429. Elapsed: $elapsed, expected >= $resetDelay"
        )
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `rate limiter respects manual time advancement`() = runTest {
        val resetDelay = 5.seconds
        val safetyMargin = 0.5.seconds

        val mockEngine = mockEngine {
            respond(
                content = ByteReadChannel("{}"),
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.RateLimitLimit to listOf("60"),
                    HttpHeaders.RateLimitRemaining to listOf("0"),
                    HttpHeaders.RateLimitReset to listOf((clock().now() + resetDelay).epochSeconds.toString()),
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

        // First request sets up rate limit data
        client.get("https://api.example.com")
        runCurrent()

        // Start a second request, which should be blocked by the rate limit
        var secondRequestCompleted = false

        // Begin the second request, but don't wait for it to complete
        async {
            client.get("https://api.example.com")
            secondRequestCompleted = true
        }

        // Check that the request hasn't completed yet
        runCurrent()
        assertFalse(secondRequestCompleted, "Request should not complete before time advancement")

        // Advance time slightly more than the reset period
        advanceTimeBy(resetDelay + safetyMargin)
        runCurrent()

        // Now the request should be complete
        assertTrue(secondRequestCompleted, "Request should complete after time advancement")
    }

    @Test
    fun `rate limiter adds safety margin to wait time`() = runTest {
        val resetDelay = 5.seconds
        val safetyMargin = 0.5.seconds

        val mockEngine = mockEngine {
            respond(
                content = ByteReadChannel("{}"),
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.RateLimitLimit to listOf("60"),
                    HttpHeaders.RateLimitRemaining to listOf("0"),
                    HttpHeaders.RateLimitReset to listOf((clock().now() + resetDelay).epochSeconds.toString()),
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

        // First request, sets up rate limit data
        client.get("https://api.example.com")

        // Second request, should wait for reset time plus safety margin
        val startTime = clock().now()
        client.get("https://api.example.com")

        val elapsed = clock().now() - startTime
        assertTrue(
            elapsed >= resetDelay + safetyMargin,
            "Should wait for reset time plus safety margin. Elapsed: $elapsed, expected >= ${resetDelay + safetyMargin}"
        )
    }
}