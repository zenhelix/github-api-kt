package io.github.zenhelix.github.client.http.rest

import io.github.zenhelix.github.client.http.GithubApi
import io.github.zenhelix.github.client.http.GithubConstants.GITHUB_API_PUBLIC_BASE_URL
import io.github.zenhelix.github.client.http.model.ErrorResponse
import io.github.zenhelix.github.client.http.model.HttpResponseResult
import io.github.zenhelix.github.client.http.model.LicensesResponse
import io.github.zenhelix.github.client.http.model.action.ArtifactResponse
import io.github.zenhelix.github.client.http.model.action.ArtifactsResponse
import io.github.zenhelix.github.client.http.model.action.CacheListResponse
import io.github.zenhelix.github.client.http.model.action.CacheUsageResponse
import io.github.zenhelix.github.client.http.model.action.DeleteCachesByKeyResponse
import io.github.zenhelix.github.client.http.model.action.Runner
import io.github.zenhelix.github.client.http.model.action.RunnerApplicationsResponse
import io.github.zenhelix.github.client.http.model.action.RunnerRegistrationToken
import io.github.zenhelix.github.client.http.model.action.RunnerRemoveToken
import io.github.zenhelix.github.client.http.model.action.RunnersResponse
import io.github.zenhelix.github.client.http.model.action.WorkflowRunArtifactsResponse
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
        name: String?,
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
        name: String?,
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
    override fun listCachesForRepository(
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

    override fun getCacheUsageForRepository(
        owner: String,
        repository: String,
        token: String?
    ): HttpResponseResult<CacheUsageResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun deleteCache(
        owner: String,
        repository: String,
        cacheId: Long,
        token: String?
    ): HttpResponseResult<Unit, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun deleteCachesByKey(
        owner: String,
        repository: String,
        key: String,
        token: String?
    ): HttpResponseResult<DeleteCachesByKeyResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun listRunnersForRepository(
        owner: String,
        repository: String,
        perPage: Int,
        page: Int,
        token: String?
    ): HttpResponseResult<RunnersResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun getRunnerForRepository(
        owner: String,
        repository: String,
        runnerId: Long,
        token: String?
    ): HttpResponseResult<Runner, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun deleteRunnerFromRepository(
        owner: String,
        repository: String,
        runnerId: Long,
        token: String?
    ): HttpResponseResult<Unit, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun createRunnerRegistrationTokenForRepository(
        owner: String,
        repository: String,
        token: String?
    ): HttpResponseResult<RunnerRegistrationToken, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun createRunnerRemoveTokenForRepository(
        owner: String,
        repository: String,
        token: String?
    ): HttpResponseResult<RunnerRemoveToken, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun listRunnerApplicationsForRepository(
        owner: String,
        repository: String,
        token: String?
    ): HttpResponseResult<RunnerApplicationsResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun listRunnersForOrganization(
        org: String,
        perPage: Int,
        page: Int,
        token: String?
    ): HttpResponseResult<RunnersResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun getRunnerForOrganization(
        org: String,
        runnerId: Long,
        token: String?
    ): HttpResponseResult<Runner, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun deleteRunnerFromOrganization(
        org: String,
        runnerId: Long,
        token: String?
    ): HttpResponseResult<Unit, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun createRunnerRegistrationTokenForOrganization(
        org: String,
        token: String?
    ): HttpResponseResult<RunnerRegistrationToken, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun createRunnerRemoveTokenForOrganization(
        org: String,
        token: String?
    ): HttpResponseResult<RunnerRemoveToken, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun listRunnerApplicationsForOrganization(
        org: String,
        token: String?
    ): HttpResponseResult<RunnerApplicationsResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun listRunnersForEnterprise(
        enterprise: String,
        perPage: Int,
        page: Int,
        token: String?
    ): HttpResponseResult<RunnersResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun getRunnerForEnterprise(
        enterprise: String,
        runnerId: Long,
        token: String?
    ): HttpResponseResult<Runner, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun deleteRunnerFromEnterprise(
        enterprise: String,
        runnerId: Long,
        token: String?
    ): HttpResponseResult<Unit, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun createRunnerRegistrationTokenForEnterprise(
        enterprise: String,
        token: String?
    ): HttpResponseResult<RunnerRegistrationToken, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun createRunnerRemoveTokenForEnterprise(
        enterprise: String,
        token: String?
    ): HttpResponseResult<RunnerRemoveToken, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun listRunnerApplicationsForEnterprise(
        enterprise: String,
        token: String?
    ): HttpResponseResult<RunnerApplicationsResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }
}
