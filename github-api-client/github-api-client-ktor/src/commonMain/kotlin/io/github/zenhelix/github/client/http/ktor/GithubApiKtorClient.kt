package io.github.zenhelix.github.client.http.ktor

import io.github.zenhelix.github.client.http.GithubConstants.GITHUB_API_PUBLIC_BASE_URL
import io.github.zenhelix.github.client.http.GithubCoroutineApi
import io.github.zenhelix.github.client.http.ktor.circuitbreaker.CircuitBreakerConfig
import io.github.zenhelix.github.client.http.ktor.circuitbreaker.CircuitBreakerException
import io.github.zenhelix.github.client.http.ktor.circuitbreaker.CircuitBreaking
import io.github.zenhelix.github.client.http.ktor.circuitbreaker.withCircuitBreaker
import io.github.zenhelix.github.client.http.ktor.ratelimiter.RateLimiting
import io.github.zenhelix.github.client.http.ktor.ratelimiter.withRateLimiter
import io.github.zenhelix.github.client.http.ktor.utils.HttpClientExtensions.result
import io.github.zenhelix.github.client.http.ktor.utils.acceptGithubJson
import io.github.zenhelix.github.client.http.ktor.utils.githubApiVersion
import io.github.zenhelix.github.client.http.model.ArtifactResponse
import io.github.zenhelix.github.client.http.model.ArtifactsResponse
import io.github.zenhelix.github.client.http.model.CircuitBreakerDataError
import io.github.zenhelix.github.client.http.model.ErrorResponse
import io.github.zenhelix.github.client.http.model.HttpResponseResult
import io.github.zenhelix.github.client.http.model.LicensesResponse
import io.github.zenhelix.github.client.http.model.WorkflowRunArtifactsResponse
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.appendPathSegments
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.toMap
import io.ktor.utils.io.toByteArray
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

public class GithubApiKtorClient(
    engine: HttpClientEngine,
    private val baseUrl: String = GITHUB_API_PUBLIC_BASE_URL,
    private val defaultToken: String? = null,
    private val clock: Clock = Clock.System,
    circuitBreakerConfig: CircuitBreakerConfig.CircuitBreakerBuilder.() -> Unit = {},
    configure: HttpClientConfig<*>.() -> Unit = {}
) : GithubCoroutineApi {

    private val client: HttpClient = HttpClient(engine) {
        followRedirects = true

        defaultRequest {
            acceptGithubJson()
            githubApiVersion()

            withRateLimiter()
            withCircuitBreaker()
        }
        install(HttpCache)
        install(ContentNegotiation) {
            json(Json { prettyPrint = true })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }
        install(CircuitBreaking) {
            global(clock) {
                failureThreshold = 4
                halfOpenFailureThreshold = 2
                resetInterval = 1.seconds
                maxHalfOpenAttempts = 5
                failureTrigger = { status.value >= 400 }

                circuitBreakerConfig()
            }
        }
        install(RateLimiting) {
            global(clock) {
                remainingThreshold = 0
                defaultResetDelay = 60.seconds
            }
        }

        configure()
    }

    override suspend fun licenses(token: String?): HttpResponseResult<LicensesResponse, ErrorResponse> = handleCircuitBreaker {
        client.get("$baseUrl/licenses") { bearerAuth(requiredToken(token)) }.result()
    }

    override suspend fun artifacts(
        owner: String,
        repository: String,
        perPage: Int,
        page: Int,
        token: String?
    ): HttpResponseResult<ArtifactsResponse, ErrorResponse> = handleCircuitBreaker {
        client.get {
            url {
                takeFrom(baseUrl).appendPathSegments("repos", owner, repository, "actions", "artifacts")
                parameters.append("per_page", perPage.toString())
                parameters.append("page", page.toString())
            }
            bearerAuth(requiredToken(token))
        }.result()
    }

    override suspend fun workflowRunArtifacts(
        owner: String,
        repository: String,
        runId: Long,
        perPage: Int,
        page: Int,
        token: String?
    ): HttpResponseResult<WorkflowRunArtifactsResponse, ErrorResponse> = handleCircuitBreaker {
        client.get {
            url {
                takeFrom(baseUrl).appendPathSegments("repos", owner, repository, "actions", "runs", runId.toString(), "artifacts")
                parameters.append("per_page", perPage.toString())
                parameters.append("page", page.toString())
            }
            bearerAuth(requiredToken(token))
        }.result()
    }

    override suspend fun artifact(
        owner: String,
        repository: String,
        artifactId: Long,
        token: String?
    ): HttpResponseResult<ArtifactResponse, ErrorResponse> = handleCircuitBreaker {
        client.get {
            url {
                takeFrom(baseUrl).appendPathSegments("repos", owner, repository, "actions", "artifacts", artifactId.toString())
            }
            bearerAuth(requiredToken(token))
        }.result()
    }

    override suspend fun deleteArtifact(
        owner: String,
        repository: String,
        artifactId: Long,
        token: String?
    ): HttpResponseResult<Unit, ErrorResponse> = handleCircuitBreaker {
        client.delete {
            url {
                takeFrom(baseUrl).appendPathSegments("repos", owner, repository, "actions", "artifacts", artifactId.toString())
            }
            bearerAuth(requiredToken(token))
        }.result()
    }

    override suspend fun downloadArtifact(
        owner: String,
        repository: String,
        artifactId: Long,
        archiveFormat: String,
        token: String?
    ): HttpResponseResult<ByteArray, ErrorResponse> = handleCircuitBreaker {
        // According to GitHub API docs, this endpoint returns a 302 redirect to a download URL
        // The client is configured to follow redirects automatically
        val response = client.get {
            url {
                takeFrom(baseUrl)
                appendPathSegments("repos", owner, repository, "actions", "artifacts", artifactId.toString(), archiveFormat)
            }
            bearerAuth(requiredToken(token))
        }

        try {
            if (response.status.value in 200..299) {
                HttpResponseResult.Success(
                    data = response.bodyAsChannel().toByteArray(),
                    httpStatus = response.status.value,
                    httpHeaders = response.headers.toMap()
                )
            } else {
                try {
                    val errorBody = response.bodyAsText()
                    val errorResponse = Json.decodeFromString<ErrorResponse>(errorBody)
                    HttpResponseResult.Error(
                        data = errorResponse,
                        httpStatus = response.status.value,
                        httpHeaders = response.headers.toMap()
                    )
                } catch (e: Exception) {
                    HttpResponseResult.UnexpectedError(
                        cause = e,
                        httpStatus = response.status.value,
                        httpHeaders = response.headers.toMap()
                    )
                }
            }
        } catch (e: Exception) {
            HttpResponseResult.UnexpectedError(
                cause = e,
                httpStatus = response.status.value,
                httpHeaders = response.headers.toMap()
            )
        }
    }

    public fun close() {
        client.close()
    }

    private fun requiredToken(token: String?): String = token ?: defaultToken ?: throw IllegalArgumentException("Token is required")

    private suspend inline fun <T : Any, E : Any> handleCircuitBreaker(
        crossinline block: suspend () -> HttpResponseResult<T, E>
    ): HttpResponseResult<T, E> = try {
        block()
    } catch (e: CircuitBreakerException) {
        HttpResponseResult.CircuitBreakerError(
            data = CircuitBreakerDataError(e.nextHalfOpenTime.toEpochMilliseconds()),
            cause = e
        )
    }
}