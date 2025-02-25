package io.github.zenhelix.github.client.http.webflux

import io.github.zenhelix.github.client.http.GithubConstants.GITHUB_API_PUBLIC_BASE_URL
import io.github.zenhelix.github.client.http.GithubCoroutineApi
import io.github.zenhelix.github.client.http.model.ArtifactResponse
import io.github.zenhelix.github.client.http.model.ArtifactsResponse
import io.github.zenhelix.github.client.http.model.CacheListResponse
import io.github.zenhelix.github.client.http.model.CacheUsageResponse
import io.github.zenhelix.github.client.http.model.DeleteCachesByKeyResponse
import io.github.zenhelix.github.client.http.model.ErrorResponse
import io.github.zenhelix.github.client.http.model.HttpResponseResult
import io.github.zenhelix.github.client.http.model.LicensesResponse
import io.github.zenhelix.github.client.http.model.WorkflowRunArtifactsResponse
import io.github.zenhelix.github.client.http.webflux.utils.acceptGithubJson
import io.github.zenhelix.github.client.http.webflux.utils.awaitResult
import io.github.zenhelix.github.client.http.webflux.utils.bearer
import io.github.zenhelix.github.client.http.webflux.utils.githubVersion
import io.github.zenhelix.github.client.http.webflux.utils.kotlinSerializationJsonCodec
import org.springframework.web.reactive.function.client.WebClient

public class GithubApiWebfluxClient(
    webClientBuilder: WebClient.Builder = WebClient.builder(),
    baseUrl: String = GITHUB_API_PUBLIC_BASE_URL,
    private val defaultToken: String? = null
) : GithubCoroutineApi {

    private val webClient: WebClient = webClientBuilder
        .baseUrl(baseUrl)
        .defaultHeaders { it.acceptGithubJson().githubVersion() }
        .kotlinSerializationJsonCodec()
        .build()

    override suspend fun licenses(token: String?): HttpResponseResult<LicensesResponse, ErrorResponse> = webClient
        .get().uri("/licenses")
        .bearer(requiredToken(token))
        .awaitResult<LicensesResponse, ErrorResponse>()

    override suspend fun artifacts(
        owner: String,
        repository: String,
        perPage: Int,
        page: Int,
        token: String?
    ): HttpResponseResult<ArtifactsResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun workflowRunArtifacts(
        owner: String,
        repository: String,
        runId: Long,
        perPage: Int,
        page: Int,
        token: String?
    ): HttpResponseResult<WorkflowRunArtifactsResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun artifact(
        owner: String,
        repository: String,
        artifactId: Long,
        token: String?
    ): HttpResponseResult<ArtifactResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteArtifact(
        owner: String,
        repository: String,
        artifactId: Long,
        token: String?
    ): HttpResponseResult<Unit, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun downloadArtifact(
        owner: String,
        repository: String,
        artifactId: Long,
        archiveFormat: String,
        token: String?
    ): HttpResponseResult<ByteArray, ErrorResponse> {
        TODO("Not yet implemented")
    }

    private fun requiredToken(token: String?): String = token ?: defaultToken ?: throw IllegalArgumentException("Token is required")
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
    ): HttpResponseResult<CacheListResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getCacheUsageForRepository(
        owner: String,
        repository: String,
        token: String?
    ): HttpResponseResult<CacheUsageResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCache(
        owner: String,
        repository: String,
        cacheId: Long,
        token: String?
    ): HttpResponseResult<Unit, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCachesByKey(
        owner: String,
        repository: String,
        key: String,
        token: String?
    ): HttpResponseResult<DeleteCachesByKeyResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

}
