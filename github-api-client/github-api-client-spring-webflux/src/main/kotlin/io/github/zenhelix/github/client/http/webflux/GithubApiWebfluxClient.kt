package io.github.zenhelix.github.client.http.webflux

import io.github.zenhelix.github.client.http.GithubConstants.GITHUB_API_PUBLIC_BASE_URL
import io.github.zenhelix.github.client.http.GithubCoroutineApi
import io.github.zenhelix.github.client.http.model.ArtifactResponse
import io.github.zenhelix.github.client.http.model.ArtifactsResponse
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

}
