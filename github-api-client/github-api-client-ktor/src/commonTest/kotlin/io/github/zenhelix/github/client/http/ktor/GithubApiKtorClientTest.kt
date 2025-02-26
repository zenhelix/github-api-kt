package io.github.zenhelix.github.client.http.ktor

import io.github.zenhelix.github.client.http.GithubApiVersion.V_2022_11_28
import io.github.zenhelix.github.client.http.GithubConstants.APPLICATION_GITHUB_JSON_MEDIA_TYPE
import io.github.zenhelix.github.client.http.GithubConstants.GITHUB_API_PUBLIC_BASE_URL
import io.github.zenhelix.github.client.http.ktor.utils.GithubApiVersion
import io.github.zenhelix.github.client.http.ktor.utils.RateLimitLimit
import io.github.zenhelix.github.client.http.ktor.utils.RateLimitRemaining
import io.github.zenhelix.github.client.http.ktor.utils.RateLimitReset
import io.github.zenhelix.github.client.http.ktor.utils.RateLimitResource
import io.github.zenhelix.github.client.http.ktor.utils.RateLimitUsed
import io.github.zenhelix.github.client.http.model.ErrorResponse
import io.github.zenhelix.github.client.http.model.HttpResponseResult
import io.github.zenhelix.github.client.http.model.license.License
import io.github.zenhelix.github.client.http.model.license.LicensesResponse
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.engine.mock.respond
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
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class GithubApiKtorClientTest {

    @Test fun `success request`() = runTest {
        val mockBearer = "mock"

        val mockEngine = mockEngine { request ->
            assertEquals("$GITHUB_API_PUBLIC_BASE_URL/licenses", request.url.toString())
            assertEquals("Bearer $mockBearer", request.headers[HttpHeaders.Authorization])
            assertEquals(APPLICATION_GITHUB_JSON_MEDIA_TYPE, request.headers[HttpHeaders.Accept])
            assertEquals(V_2022_11_28.version, request.headers[HttpHeaders.GithubApiVersion])

            respond(
                content = ByteReadChannel(
                    //language=JSON
                    """[{"key":"agpl-3.0","name":"GNU Affero General Public License v3.0","spdx_id":"AGPL-3.0","url":"https://api.github.com/licenses/agpl-3.0","node_id":"MDc6TGljZW5zZTE="},{"key":"apache-2.0","name":"Apache License 2.0","spdx_id":"Apache-2.0","url":"https://api.github.com/licenses/apache-2.0","node_id":"MDc6TGljZW5zZTI="},{"key":"bsd-2-clause","name":"BSD 2-Clause \"Simplified\" License","spdx_id":"BSD-2-Clause","url":"https://api.github.com/licenses/bsd-2-clause","node_id":"MDc6TGljZW5zZTQ="},{"key":"bsd-3-clause","name":"BSD 3-Clause \"New\" or \"Revised\" License","spdx_id":"BSD-3-Clause","url":"https://api.github.com/licenses/bsd-3-clause","node_id":"MDc6TGljZW5zZTU="},{"key":"bsl-1.0","name":"Boost Software License 1.0","spdx_id":"BSL-1.0","url":"https://api.github.com/licenses/bsl-1.0","node_id":"MDc6TGljZW5zZTI4"},{"key":"cc0-1.0","name":"Creative Commons Zero v1.0 Universal","spdx_id":"CC0-1.0","url":"https://api.github.com/licenses/cc0-1.0","node_id":"MDc6TGljZW5zZTY="},{"key":"epl-2.0","name":"Eclipse Public License 2.0","spdx_id":"EPL-2.0","url":"https://api.github.com/licenses/epl-2.0","node_id":"MDc6TGljZW5zZTMy"},{"key":"gpl-2.0","name":"GNU General Public License v2.0","spdx_id":"GPL-2.0","url":"https://api.github.com/licenses/gpl-2.0","node_id":"MDc6TGljZW5zZTg="},{"key":"gpl-3.0","name":"GNU General Public License v3.0","spdx_id":"GPL-3.0","url":"https://api.github.com/licenses/gpl-3.0","node_id":"MDc6TGljZW5zZTk="},{"key":"lgpl-2.1","name":"GNU Lesser General Public License v2.1","spdx_id":"LGPL-2.1","url":"https://api.github.com/licenses/lgpl-2.1","node_id":"MDc6TGljZW5zZTEx"},{"key":"mit","name":"MIT License","spdx_id":"MIT","url":"https://api.github.com/licenses/mit","node_id":"MDc6TGljZW5zZTEz"},{"key":"mpl-2.0","name":"Mozilla Public License 2.0","spdx_id":"MPL-2.0","url":"https://api.github.com/licenses/mpl-2.0","node_id":"MDc6TGljZW5zZTE0"},{"key":"unlicense","name":"The Unlicense","spdx_id":"Unlicense","url":"https://api.github.com/licenses/unlicense","node_id":"MDc6TGljZW5zZTE1"}]"""
                ),
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.AcceptRanges to listOf("bytes"),
                    HttpHeaders.AccessControlAllowOrigin to listOf("*"),
                    HttpHeaders.AccessControlExposeHeaders to listOf(
                        "ETag, Link, Location, Retry-After, X-GitHub-OTP, X-RateLimit-Limit, X-RateLimit-Remaining, X-RateLimit-Used, X-RateLimit-Resource, X-RateLimit-Reset, X-OAuth-Scopes, X-Accepted-OAuth-Scopes, X-Poll-Interval, X-GitHub-Media-Type, X-GitHub-SSO, X-GitHub-Request-Id, Deprecation, Sunset"
                    ),
                    HttpHeaders.CacheControl to listOf("public", "max-age=60", "s-maxage=60"),
                    HttpHeaders.ContentLength to listOf("2037"),
                    "Content-Security-Policy:" to listOf("default-src 'none'"),
                    HttpHeaders.ContentType to listOf("application/json"),
                    HttpHeaders.Date to listOf("Wed, 05 Feb 2025 15:52:34 GMT"),
                    HttpHeaders.ETag to listOf("W/\"f0483b0fa41fbbfed05fe7cca58b2bf52789eb6cf225b5be7b921e70554d48f4\""),
                    "Referrer-Policy" to listOf("origin-when-cross-origin", "strict-origin-when-cross-origin"),
                    HttpHeaders.Server to listOf("github.com"),
                    HttpHeaders.StrictTransportSecurity to listOf("max-age=31536000; includeSubdomains; preload"),
                    HttpHeaders.Vary to listOf("Accept", "Accept-Encoding", "X-Requested-With"),
                    "X-Content-Type-Options" to listOf("nosniff"),
                    "X-Frame-Options" to listOf("deny"),
                    "X-GitHub-Media-Type" to listOf("github.v3"),
                    "X-GitHub-Request-Id" to listOf("577E:9FCA3:11300B:17B6A3:67A38942"),
                    HttpHeaders.RateLimitLimit to listOf("60"),
                    HttpHeaders.RateLimitRemaining to listOf("33"),
                    HttpHeaders.RateLimitReset to listOf("1738772393"),
                    HttpHeaders.RateLimitResource to listOf("core"),
                    HttpHeaders.RateLimitUsed to listOf("27"),
                    "X-XSS-Protection" to listOf("0"),
                    "x-github-api-version-selected" to listOf("2022-11-28")
                )
            )
        }

        val result = GithubApiKtorClient(mockEngine, clock = clock(), defaultToken = mockBearer).licenses()
        assertEquals((result as HttpResponseResult.Success).httpStatus, 200)
        assertTrue(result.httpHeaders.size == 24)
        assertEquals(
            listOf(
                License(
                    key = "agpl-3.0",
                    name = "GNU Affero General Public License v3.0",
                    spdxId = "AGPL-3.0",
                    url = "https://api.github.com/licenses/agpl-3.0",
                    nodeId = "MDc6TGljZW5zZTE="
                ),
                License(
                    key = "apache-2.0",
                    name = "Apache License 2.0",
                    spdxId = "Apache-2.0",
                    url = "https://api.github.com/licenses/apache-2.0",
                    nodeId = "MDc6TGljZW5zZTI="
                ),
                License(
                    key = "bsd-2-clause",
                    name = "BSD 2-Clause \"Simplified\" License",
                    spdxId = "BSD-2-Clause",
                    url = "https://api.github.com/licenses/bsd-2-clause",
                    nodeId = "MDc6TGljZW5zZTQ="
                ),
                License(
                    key = "bsd-3-clause",
                    name = "BSD 3-Clause \"New\" or \"Revised\" License",
                    spdxId = "BSD-3-Clause",
                    url = "https://api.github.com/licenses/bsd-3-clause",
                    nodeId = "MDc6TGljZW5zZTU="
                ),
                License(
                    key = "bsl-1.0",
                    name = "Boost Software License 1.0",
                    spdxId = "BSL-1.0",
                    url = "https://api.github.com/licenses/bsl-1.0",
                    nodeId = "MDc6TGljZW5zZTI4"
                ),
                License(
                    key = "cc0-1.0",
                    name = "Creative Commons Zero v1.0 Universal",
                    spdxId = "CC0-1.0",
                    url = "https://api.github.com/licenses/cc0-1.0",
                    nodeId = "MDc6TGljZW5zZTY="
                ),
                License(
                    key = "epl-2.0",
                    name = "Eclipse Public License 2.0",
                    spdxId = "EPL-2.0",
                    url = "https://api.github.com/licenses/epl-2.0",
                    nodeId = "MDc6TGljZW5zZTMy"
                ),
                License(
                    key = "gpl-2.0",
                    name = "GNU General Public License v2.0",
                    spdxId = "GPL-2.0",
                    url = "https://api.github.com/licenses/gpl-2.0",
                    nodeId = "MDc6TGljZW5zZTg="
                ),
                License(
                    key = "gpl-3.0",
                    name = "GNU General Public License v3.0",
                    spdxId = "GPL-3.0",
                    url = "https://api.github.com/licenses/gpl-3.0",
                    nodeId = "MDc6TGljZW5zZTk="
                ),
                License(
                    key = "lgpl-2.1",
                    name = "GNU Lesser General Public License v2.1",
                    spdxId = "LGPL-2.1",
                    url = "https://api.github.com/licenses/lgpl-2.1",
                    nodeId = "MDc6TGljZW5zZTEx"
                ),
                License(key = "mit", name = "MIT License", spdxId = "MIT", url = "https://api.github.com/licenses/mit", nodeId = "MDc6TGljZW5zZTEz"),
                License(
                    key = "mpl-2.0",
                    name = "Mozilla Public License 2.0",
                    spdxId = "MPL-2.0",
                    url = "https://api.github.com/licenses/mpl-2.0",
                    nodeId = "MDc6TGljZW5zZTE0"
                ),
                License(
                    key = "unlicense",
                    name = "The Unlicense",
                    spdxId = "Unlicense",
                    url = "https://api.github.com/licenses/unlicense",
                    nodeId = "MDc6TGljZW5zZTE1"
                )
            ),
            result.result()
        )
    }

    @Test fun `unexpected response`() = runTest {
        val mockEngine = mockEngine {
            respond(
                content = ByteReadChannel("unexpected"),
                status = HttpStatusCode.OK
            )
        }

        val result = GithubApiKtorClient(mockEngine, clock = clock(), defaultToken = "mock").licenses()
        assertTrue(result is HttpResponseResult.UnexpectedError)
        assertTrue(result.cause is NoTransformationFoundException)
    }

    @Test fun `error response`() = runTest {
        val mockEngine = mockEngine {
            respond(
                content = ByteReadChannel(
                    //language=JSON
                    """{"message":"Bad credentials","documentation_url":"https://docs.github.com/rest","status":"401"}"""
                ),
                status = HttpStatusCode.Unauthorized,
                headers = headersOf(HttpHeaders.ContentType to listOf("application/json; charset=utf-8"))
            )
        }

        val result = GithubApiKtorClient(mockEngine, clock = clock(), defaultToken = "mock").licenses()
        assertTrue(result is HttpResponseResult.Error<*>)
        assertEquals(
            ErrorResponse(
                message = "Bad credentials",
                documentationUrl = "https://docs.github.com/rest",
                status = "401"
            ),
            result.data as ErrorResponse
        )
    }

    @Test fun `circuit breaker`() = runTest {
        val resetInterval = 10.seconds
        val failureThreshold = 3

        val mockEngine = createMockEngine {
            repeat(failureThreshold + 1) {
                addHandler {
                    respond(
                        content = ByteReadChannel(
                            //language=JSON
                            """{"message":"Bad credentials","documentation_url":"https://docs.github.com/rest","status":"401"}"""
                        ),
                        status = HttpStatusCode.Unauthorized,
                        headers = headersOf(HttpHeaders.ContentType to listOf("application/json; charset=utf-8"))
                    )
                }
            }

            addHandler {
                respond(
                    //language=JSON
                    "[]",
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType to listOf("application/json; charset=utf-8"))
                )
            }
        }

        val client = GithubApiKtorClient(mockEngine, clock = clock(), defaultToken = "mock", circuitBreakerConfig = {
            this.failureThreshold = failureThreshold
            this.halfOpenFailureThreshold = 2
            this.resetInterval = resetInterval
        })

        val expectedResponse = ErrorResponse(message = "Bad credentials", documentationUrl = "https://docs.github.com/rest", status = "401")

        repeat(failureThreshold) {
            assertEquals(expectedResponse, (client.licenses() as HttpResponseResult.Error<ErrorResponse>).data)
        }

        assertTrue(client.licenses() is HttpResponseResult.CircuitBreakerError)

        advanceTimeBy(resetInterval + 1.seconds)
        runCurrent()

        assertEquals(expectedResponse, (client.licenses() as HttpResponseResult.Error<ErrorResponse>).data)

        assertEquals(
            emptyList<License>(),
            (client.licenses() as HttpResponseResult.Success<LicensesResponse>).data
        )
    }

    @Test
    fun `circuit breaker limits concurrent requests in half-open state`() = runTest {
        val resetInterval = 10.seconds
        val failureThreshold = 3
        val maxHalfOpenAttempts = 2 // Only allow 2 concurrent requests in HALF_OPEN state

        val mockEngine = createMockEngine {
            // First set of requests - errors to trigger circuit opening
            repeat(failureThreshold) {
                addHandler {
                    respond(
                        content = ByteReadChannel(
                            //language=JSON
                            """{"message":"Server Error","documentation_url":"https://docs.github.com/rest","status":"500"}"""
                        ),
                        status = HttpStatusCode.InternalServerError,
                        headers = headersOf(HttpHeaders.ContentType to listOf("application/json; charset=utf-8"))
                    )
                }
            }

            // After circuit opens and resets to HALF_OPEN, these will be the responses
            // Add more than maxHalfOpenAttempts successful responses
            repeat(maxHalfOpenAttempts + 3) {
                addHandler {
                    respond(
                        content = ByteReadChannel(
                            //language=JSON
                            "[]"
                        ),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType to listOf("application/json; charset=utf-8"))
                    )
                }
            }
        }

        val client = GithubApiKtorClient(mockEngine, clock = clock(), defaultToken = "mock", circuitBreakerConfig = {
            this.failureThreshold = failureThreshold
            this.resetInterval = resetInterval
            this.maxHalfOpenAttempts = maxHalfOpenAttempts
        })

        val expectedErrorResponse = ErrorResponse(
            message = "Server Error",
            documentationUrl = "https://docs.github.com/rest",
            status = "500"
        )

        // Make enough failing requests to open the circuit
        repeat(failureThreshold) {
            val result = client.licenses()
            assertTrue(result is HttpResponseResult.Error)
            assertEquals(expectedErrorResponse, result.data)
        }

        // Next request should be blocked by the open circuit
        val circuitBlockedResult = client.licenses()
        assertTrue(circuitBlockedResult is HttpResponseResult.CircuitBreakerError)

        // Advance time to allow circuit breaker to transition to HALF_OPEN
        advanceTimeBy(resetInterval + 1.seconds)
        runCurrent()

        // Create concurrent requests that exceed maxHalfOpenAttempts
        val results = mutableListOf<HttpResponseResult<*, *>>()
        val concurrentRequests = maxHalfOpenAttempts + 2 // Exceed the limit by 2

        // Launch concurrent requests
        val deferreds = List(concurrentRequests) {
            async {
                client.licenses()
            }
        }

        // Wait for all requests to complete
        runCurrent()
        deferreds.forEach { deferred ->
            results.add(deferred.await())
        }

        // Count successful requests and circuit breaker errors
        val successCount = results.count { it is HttpResponseResult.Success }
        val circuitBreakerErrorCount = results.count { it is HttpResponseResult.CircuitBreakerError }

        // Verify only maxHalfOpenAttempts succeeded, others got circuit breaker errors
        assertEquals(
            maxHalfOpenAttempts, successCount,
            "Only $maxHalfOpenAttempts requests should succeed in HALF_OPEN state"
        )
        assertEquals(
            concurrentRequests - maxHalfOpenAttempts, circuitBreakerErrorCount,
            "Requests exceeding maxHalfOpenAttempts should be rejected with CircuitBreakerError"
        )
    }

    @Test fun `rate limiter without header`() = runTest {
        val mockEngine = mockEngine {
            respond(
                //language=JSON
                "[]",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType to listOf("application/json; charset=utf-8"))
            )
        }

        val client = GithubApiKtorClient(mockEngine, clock = clock(), defaultToken = "mock")

        repeat(2) {
            assertEquals(
                emptyList<License>(),
                (client.licenses() as HttpResponseResult.Success<LicensesResponse>).data
            )
        }
    }

    @Test fun `rate limiter`() = runTest {
        val delay = 2.minutes

        val mockEngine = mockEngine {
            respond(
                //language=JSON
                "[]",
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.ContentType to listOf("application/json; charset=utf-8"),
                    HttpHeaders.RateLimitLimit to listOf("60"),
                    HttpHeaders.RateLimitRemaining to listOf("0"),
                    HttpHeaders.RateLimitReset to listOf((clock().now() + delay).epochSeconds.toString()),
                    HttpHeaders.RateLimitResource to listOf("core"),
                    HttpHeaders.RateLimitUsed to listOf("27")
                )
            )
        }

        val client = GithubApiKtorClient(mockEngine, defaultToken = "mock", clock = clock())

        assertEquals(emptyList<License>(), (client.licenses() as HttpResponseResult.Success<LicensesResponse>).data)
        runCurrent()

        val startTime = clock().now()

        assertEquals(emptyList<License>(), (client.licenses() as HttpResponseResult.Success<LicensesResponse>).data)

        val elapsed = clock().now() - startTime
        assertTrue(
            elapsed >= delay,
            "Should have waited for rate limit reset. Elapsed: ${elapsed.inWholeSeconds}s, expected >= ${delay.inWholeSeconds}s"
        )
    }

    @Test
    fun `downloadArtifact - follows redirect and returns content`() = runTest {
        val mockBearer = "mock-token"
        val owner = "testOwner"
        val repository = "testRepo"
        val artifactId = 12345L
        val archiveFormat = "zip"
        val redirectUrl = "https://storage.example.com/artifact-download-url"

        val testContent = "Test artifact content".encodeToByteArray()

        val mockEngine = createMockEngine {
            addHandler { request ->
                assertEquals("$GITHUB_API_PUBLIC_BASE_URL/repos/$owner/$repository/actions/artifacts/$artifactId/$archiveFormat", request.url.toString())
                assertEquals("Bearer $mockBearer", request.headers[HttpHeaders.Authorization])
                assertEquals(APPLICATION_GITHUB_JSON_MEDIA_TYPE, request.headers[HttpHeaders.Accept])
                assertEquals(V_2022_11_28.version, request.headers[HttpHeaders.GithubApiVersion])

                respond(
                    content = ByteReadChannel(""),
                    status = HttpStatusCode.Found,
                    headers = headersOf(HttpHeaders.Location to listOf(redirectUrl))
                )
            }

            // Second request - actual download from the redirect URL
            addHandler { request ->
                assertEquals(redirectUrl, request.url.toString())

                respond(
                    content = ByteReadChannel(testContent),
                    status = HttpStatusCode.OK,
                    headers = headersOf(
                        HttpHeaders.ContentType to listOf("application/zip"),
                        HttpHeaders.ContentDisposition to listOf("attachment; filename=artifact.zip"),
                        HttpHeaders.ContentLength to listOf(testContent.size.toString())
                    )
                )
            }
        }

        val client = GithubApiKtorClient(mockEngine, clock = clock(), defaultToken = mockBearer)

        val result = client.downloadArtifact(owner, repository, artifactId, archiveFormat)

        assertEquals(200, result.httpStatus)
        assertTrue(result is HttpResponseResult.Success)
        assertContentEquals(testContent, result.data)
    }

    @Test
    fun `downloadArtifact - error before redirect`() = runTest {
        val mockBearer = "mock-token"
        val owner = "testOwner"
        val repository = "testRepo"
        val artifactId = 12345L
        val archiveFormat = "zip"

        val mockEngine = mockEngine { request ->
            // Return an error response instead of a redirect
            respond(
                content = ByteReadChannel(
                    //language=JSON
                    """{"message":"Artifact not found","documentation_url":"https://docs.github.com/rest","status":"404"}"""
                ),
                status = HttpStatusCode.NotFound,
                headers = headersOf(HttpHeaders.ContentType to listOf("application/json; charset=utf-8"))
            )
        }

        val client = GithubApiKtorClient(mockEngine, clock = clock(), defaultToken = mockBearer)

        val result = client.downloadArtifact(owner, repository, artifactId, archiveFormat)

        assertTrue(result is HttpResponseResult.Error<*>)
        assertEquals(404, result.httpStatus)
        assertEquals(
            ErrorResponse(message = "Artifact not found", documentationUrl = "https://docs.github.com/rest", status = "404"),
            (result as HttpResponseResult.Error<ErrorResponse>).data
        )
    }

    @Test
    fun `downloadArtifact - error during download after redirect`() = runTest {
        val mockBearer = "mock-token"
        val owner = "testOwner"
        val repository = "testRepo"
        val artifactId = 12345L
        val archiveFormat = "zip"
        val redirectUrl = "https://storage.example.com/artifact-download-url"

        val mockEngine = createMockEngine {
            // First request returns a redirect
            addHandler { request ->
                respond(
                    content = "",
                    status = HttpStatusCode.Found,
                    headers = headersOf(HttpHeaders.Location to listOf(redirectUrl))
                )
            }

            // Second request (follow redirect) returns an error
            addHandler { request ->
                respond(
                    content = "Access denied or expired download URL",
                    status = HttpStatusCode.Forbidden,
                    headers = headersOf(HttpHeaders.ContentType to listOf("text/plain"))
                )
            }
        }

        val client = GithubApiKtorClient(mockEngine, clock = clock(), defaultToken = mockBearer)

        val result = client.downloadArtifact(owner, repository, artifactId, archiveFormat)

        assertTrue(result is HttpResponseResult.UnexpectedError)
        assertEquals(403, result.httpStatus)
    }

}