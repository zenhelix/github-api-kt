package io.github.zenhelix.github.client.http.rest

import io.github.zenhelix.github.client.http.GithubApi
import io.github.zenhelix.github.client.http.GithubConstants.GITHUB_API_PUBLIC_BASE_URL
import io.github.zenhelix.github.client.http.model.ArtifactResponse
import io.github.zenhelix.github.client.http.model.ArtifactsResponse
import io.github.zenhelix.github.client.http.model.ErrorResponse
import io.github.zenhelix.github.client.http.model.HttpResponseResult
import io.github.zenhelix.github.client.http.model.LicensesResponse
import io.github.zenhelix.github.client.http.model.WorkflowRunArtifactsResponse
import io.github.zenhelix.github.client.http.rest.utils.acceptGithubJson
import io.github.zenhelix.github.client.http.rest.utils.bearer
import io.github.zenhelix.github.client.http.rest.utils.githubVersion
import io.github.zenhelix.github.client.http.rest.utils.result
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter
import org.springframework.web.client.RestClient

public class GithubApiRestClient(
    restClientBuilder: RestClient.Builder = RestClient.builder(),
    baseUrl: String = GITHUB_API_PUBLIC_BASE_URL,
    private val defaultToken: String? = null
) : GithubApi {

    private val restClient = restClientBuilder
        .baseUrl(baseUrl)
        .defaultHeaders { it.acceptGithubJson().githubVersion() }
        .messageConverters { it.addFirst(KotlinSerializationJsonHttpMessageConverter()) }
        .build()

    override fun licenses(token: String?): HttpResponseResult<LicensesResponse, ErrorResponse> = restClient
        .get().uri("/licenses")
        .bearer(requiredToken(token))
        .result()


    override fun artifacts(
        owner: String,
        repository: String,
        perPage: Int,
        page: Int,
        token: String?
    ): HttpResponseResult<ArtifactsResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun workflowRunArtifacts(
        owner: String,
        repository: String,
        runId: Long,
        perPage: Int,
        page: Int,
        token: String?
    ): HttpResponseResult<WorkflowRunArtifactsResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun artifact(
        owner: String,
        repository: String,
        artifactId: Long,
        token: String?
    ): HttpResponseResult<ArtifactResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun deleteArtifact(
        owner: String,
        repository: String,
        artifactId: Long,
        token: String?
    ): HttpResponseResult<Unit, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun downloadArtifact(
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
