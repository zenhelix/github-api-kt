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
import io.github.zenhelix.github.client.http.model.CircuitBreakerDataError
import io.github.zenhelix.github.client.http.model.ErrorResponse
import io.github.zenhelix.github.client.http.model.HttpResponseResult
import io.github.zenhelix.github.client.http.model.action.ArtifactResponse
import io.github.zenhelix.github.client.http.model.action.ArtifactsResponse
import io.github.zenhelix.github.client.http.model.action.CacheListResponse
import io.github.zenhelix.github.client.http.model.action.CacheUsageResponse
import io.github.zenhelix.github.client.http.model.action.DeleteCachesByKeyResponse
import io.github.zenhelix.github.client.http.model.action.HostedRunner
import io.github.zenhelix.github.client.http.model.action.HostedRunnersResponse
import io.github.zenhelix.github.client.http.model.action.OrganizationCacheUsageResponse
import io.github.zenhelix.github.client.http.model.action.OrganizationRepositoriesCacheUsageResponse
import io.github.zenhelix.github.client.http.model.action.RunnerImagesResponse
import io.github.zenhelix.github.client.http.model.action.RunnerLimitsResponse
import io.github.zenhelix.github.client.http.model.action.RunnerMachineSpecsResponse
import io.github.zenhelix.github.client.http.model.action.RunnerPlatformsResponse
import io.github.zenhelix.github.client.http.model.action.WorkflowRunArtifactsResponse
import io.github.zenhelix.github.client.http.model.license.LicensesResponse
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
        name: String?,
        perPage: Int,
        page: Int,
        token: String?
    ): HttpResponseResult<ArtifactsResponse, ErrorResponse> = handleCircuitBreaker {
        client.get {
            url {
                takeFrom(baseUrl).appendPathSegments("repos", owner, repository, "actions", "artifacts")
                parameters.append("per_page", perPage.toString())
                parameters.append("page", page.toString())
                name?.let { parameters.append("name", it) }
            }
            bearerAuth(requiredToken(token))
        }.result()
    }

    override suspend fun workflowRunArtifacts(
        owner: String,
        repository: String,
        runId: Long,
        name: String?,
        perPage: Int,
        page: Int,
        token: String?
    ): HttpResponseResult<WorkflowRunArtifactsResponse, ErrorResponse> = handleCircuitBreaker {
        client.get {
            url {
                takeFrom(baseUrl).appendPathSegments("repos", owner, repository, "actions", "runs", runId.toString(), "artifacts")
                parameters.append("per_page", perPage.toString())
                parameters.append("page", page.toString())
                name?.let { parameters.append("name", it) }
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

    override suspend fun listCachesForRepository(
        owner: String,
        repository: String,
        key: String?,
        ref: String?,
        sort: String,
        direction: String,
        perPage: Int,
        page: Int,
        token: String?
    ): HttpResponseResult<CacheListResponse, ErrorResponse> = handleCircuitBreaker {
        client.get {
            url {
                takeFrom(baseUrl).appendPathSegments("repos", owner, repository, "actions", "caches")
                key?.let { parameters.append("key", it) }
                ref?.let { parameters.append("ref", it) }
                parameters.append("sort", sort)
                parameters.append("direction", direction)
                parameters.append("per_page", perPage.toString())
                parameters.append("page", page.toString())
            }
            bearerAuth(requiredToken(token))
        }.result()
    }

    override suspend fun getCacheUsageForRepository(
        owner: String,
        repository: String,
        token: String?
    ): HttpResponseResult<CacheUsageResponse, ErrorResponse> = handleCircuitBreaker {
        client.get {
            url {
                takeFrom(baseUrl).appendPathSegments("repos", owner, repository, "actions", "cache", "usage")
            }
            bearerAuth(requiredToken(token))
        }.result()
    }

    override suspend fun deleteCache(
        owner: String,
        repository: String,
        cacheId: Long,
        token: String?
    ): HttpResponseResult<Unit, ErrorResponse> = handleCircuitBreaker {
        client.delete {
            url {
                takeFrom(baseUrl).appendPathSegments("repos", owner, repository, "actions", "caches", cacheId.toString())
            }
            bearerAuth(requiredToken(token))
        }.result()
    }

    override suspend fun deleteCachesByKey(
        owner: String,
        repository: String,
        key: String,
        ref: String?,
        token: String?
    ): HttpResponseResult<DeleteCachesByKeyResponse, ErrorResponse> = handleCircuitBreaker {
        client.delete {
            url {
                takeFrom(baseUrl).appendPathSegments("repos", owner, repository, "actions", "caches")
                parameters.append("key", key)
                ref?.let { parameters.append("ref", it) }
            }
            bearerAuth(requiredToken(token))
        }.result()
    }

    override suspend fun getCacheUsageForOrganization(
        org: String,
        token: String?
    ): HttpResponseResult<OrganizationCacheUsageResponse, ErrorResponse> = handleCircuitBreaker {
        client.get {
            url { takeFrom(baseUrl).appendPathSegments("orgs", org, "actions", "cache", "usage") }
            bearerAuth(requiredToken(token))
        }.result()
    }

    override suspend fun listRepositoriesWithCacheUsageForOrganization(
        org: String,
        perPage: Int,
        page: Int,
        token: String?
    ): HttpResponseResult<OrganizationRepositoriesCacheUsageResponse, ErrorResponse> = handleCircuitBreaker {
        client.get {
            url {
                takeFrom(baseUrl).appendPathSegments("orgs", org, "actions", "cache", "usage-by-repository")
                parameters.append("per_page", perPage.toString())
                parameters.append("page", page.toString())
            }
            bearerAuth(requiredToken(token))
        }.result()
    }

    override suspend fun listHostedRunners(
        org: String,
        perPage: Int,
        page: Int,
        token: String?
    ): HttpResponseResult<HostedRunnersResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun createHostedRunner(
        org: String,
        name: String,
        image: Map<String, String>,
        size: String,
        runnerGroupId: Long,
        maximumRunners: Int?,
        enableStaticIp: Boolean?,
        token: String?
    ): HttpResponseResult<HostedRunner, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getGitHubOwnedImages(
        org: String,
        token: String?
    ): HttpResponseResult<RunnerImagesResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getPartnerImages(
        org: String,
        token: String?
    ): HttpResponseResult<RunnerImagesResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getHostedRunnersLimits(
        org: String,
        token: String?
    ): HttpResponseResult<RunnerLimitsResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getHostedRunnersMachineSpecs(
        org: String,
        token: String?
    ): HttpResponseResult<RunnerMachineSpecsResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getHostedRunnersPlatforms(
        org: String,
        token: String?
    ): HttpResponseResult<RunnerPlatformsResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getHostedRunner(
        org: String,
        runnerId: Long,
        token: String?
    ): HttpResponseResult<HostedRunner, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun updateHostedRunner(
        org: String,
        runnerId: Long,
        name: String?,
        runnerGroupId: Long?,
        maximumRunners: Int?,
        enableStaticIp: Boolean?,
        imageVersion: String?,
        token: String?
    ): HttpResponseResult<HostedRunner, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteHostedRunner(
        org: String,
        runnerId: Long,
        token: String?
    ): HttpResponseResult<HostedRunner, ErrorResponse> {
        TODO("Not yet implemented")
    }

}

