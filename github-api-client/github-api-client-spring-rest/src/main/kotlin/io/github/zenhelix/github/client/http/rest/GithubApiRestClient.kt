package io.github.zenhelix.github.client.http.rest

import io.github.zenhelix.github.client.http.GithubApi
import io.github.zenhelix.github.client.http.GithubConstants.GITHUB_API_PUBLIC_BASE_URL
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
        ref: String?,
        token: String?
    ): HttpResponseResult<DeleteCachesByKeyResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun getCacheUsageForOrganization(
        org: String,
        token: String?
    ): HttpResponseResult<OrganizationCacheUsageResponse, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override fun listRepositoriesWithCacheUsageForOrganization(
        org: String,
        perPage: Int,
        page: Int,
        token: String?
    ): HttpResponseResult<OrganizationRepositoriesCacheUsageResponse, ErrorResponse> {
        TODO("Not yet implemented")
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
