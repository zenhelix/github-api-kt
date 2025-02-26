package io.github.zenhelix.github.client.http.api.action

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

public interface GithubActionsApi : GithubArtifactApi, GithubCacheApi, GithubHostedRunnersApi, GithubOIDCApi, GithubPermissionsApi, GithubSecretsApi,
                                    GithubRunnerGroupsApi, GithubVariablesApi, GithubWorkflowJobsApi, GithubWorkflowRunsApi, GithubWorkflowsApi

public interface GithubArtifactApi {

    /**
     * Lists all artifacts for a repository.
     *
     * @param owner The account owner of the repository. The name is not case-sensitive.
     * @param repository The name of the repository without the .git extension. The name is not case-sensitive.
     * @param perPage The number of results per page (max 100). Default: 30
     * @param page Page number of the results to fetch. Default: 1
     * @param name The name field of an artifact. When specified, only artifacts with this name will be returned.
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the list of artifacts or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/artifacts?apiVersion=2022-11-28#list-artifacts-for-a-repository)
     */
    public fun artifacts(
        owner: String,
        repository: String,
        name: String? = null,
        perPage: Int = 30,
        page: Int = 1,
        token: String? = null
    ): HttpResponseResult<ArtifactsResponse, ErrorResponse>

    /**
     * Lists artifacts for a workflow run.
     *
     * @param owner The account owner of the repository. The name is not case-sensitive.
     * @param repository The name of the repository without the .git extension. The name is not case-sensitive.
     * @param runId The unique identifier of the workflow run.
     * @param perPage The number of results per page (max 100). Default: 30
     * @param page Page number of the results to fetch. Default: 1
     * @param name The name field of an artifact. When specified, only artifacts with this name will be returned.
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the list of artifacts or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/artifacts?apiVersion=2022-11-28#list-workflow-run-artifacts)
     */
    public fun workflowRunArtifacts(
        owner: String,
        repository: String,
        runId: Long,
        name: String? = null,
        perPage: Int = 30,
        page: Int = 1,
        token: String? = null
    ): HttpResponseResult<WorkflowRunArtifactsResponse, ErrorResponse>

    /**
     * Gets a specific artifact for a workflow run.
     *
     * @param owner The account owner of the repository. The name is not case-sensitive.
     * @param repository The name of the repository without the .git extension. The name is not case-sensitive.
     * @param artifactId The unique identifier of the artifact.
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the artifact details or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/artifacts?apiVersion=2022-11-28#get-an-artifact)
     */
    public fun artifact(
        owner: String,
        repository: String,
        artifactId: Long,
        token: String? = null
    ): HttpResponseResult<ArtifactResponse, ErrorResponse>

    /**
     * Deletes an artifact.
     *
     * @param owner The account owner of the repository. The name is not case-sensitive.
     * @param repository The name of the repository without the .git extension. The name is not case-sensitive.
     * @param artifactId The unique identifier of the artifact.
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] indicating success or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/artifacts?apiVersion=2022-11-28#delete-an-artifact)
     */
    public fun deleteArtifact(
        owner: String,
        repository: String,
        artifactId: Long,
        token: String? = null
    ): HttpResponseResult<Unit, ErrorResponse>

    /**
     * Downloads an artifact.
     *
     * @param owner The account owner of the repository. The name is not case-sensitive.
     * @param repository The name of the repository without the .git extension. The name is not case-sensitive.
     * @param artifactId The unique identifier of the artifact.
     * @param archiveFormat The format of the downloaded artifact. One of: 'zip' or 'tar'. Default: 'zip'
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the artifact data as a ByteArray or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/artifacts?apiVersion=2022-11-28#download-an-artifact)
     */
    public fun downloadArtifact(
        owner: String,
        repository: String,
        artifactId: Long,
        archiveFormat: String = "zip",
        token: String? = null
    ): HttpResponseResult<ByteArray, ErrorResponse>

}

/**
 * Synchronous interface for GitHub Actions Cache API operations.
 */
public interface GithubCacheApi {

    /**
     * Lists GitHub Actions caches for a repository.
     *
     * @param owner Repository owner's username.
     * @param repository Repository name.
     * @param key Optional cache key or prefix for filtering.
     * @param ref Optional Git reference to narrow down the cache.
     * @param sort Property to sort results by. Default is "last_accessed_at".
     * @param direction Sort direction. Default is "desc".
     * @param perPage Number of results per page (max 100). Default is 30.
     * @param page Page number of results. Default is 1.
     * @param token Optional authentication token.
     * @return List of caches or an error response.
     */
    public fun listCachesForRepository(
        owner: String,
        repository: String,
        key: String? = null,
        ref: String? = null,
        sort: String = "last_accessed_at",
        direction: String = "desc",
        perPage: Int = 30,
        page: Int = 1,
        token: String? = null
    ): HttpResponseResult<CacheListResponse, ErrorResponse>

    /**
     * Gets GitHub Actions cache usage for a repository.
     *
     * @param owner Repository owner's username.
     * @param repository Repository name.
     * @param token Optional authentication token.
     * @return Cache usage statistics or an error response.
     */
    public fun getCacheUsageForRepository(
        owner: String,
        repository: String,
        token: String? = null
    ): HttpResponseResult<CacheUsageResponse, ErrorResponse>

    /**
     * Deletes a specific GitHub Actions cache for a repository by its ID.
     *
     * @param owner Repository owner's username.
     * @param repository Repository name.
     * @param cacheId Unique identifier of the cache to delete.
     * @param token Optional authentication token.
     * @return Success indication or an error response.
     */
    public fun deleteCache(
        owner: String,
        repository: String,
        cacheId: Long,
        token: String? = null
    ): HttpResponseResult<Unit, ErrorResponse>

    /**
     * Deletes GitHub Actions caches for a repository by a specific key.
     *
     * @param owner Repository owner's username.
     * @param repository Repository name.
     * @param key Key identifying the caches to delete.
     * @param ref Optional Git reference to further narrow down cache deletion.
     * @param token Optional authentication token.
     * @return Details of deleted caches or an error response.
     */
    public fun deleteCachesByKey(
        owner: String,
        repository: String,
        key: String,
        ref: String? = null,
        token: String? = null
    ): HttpResponseResult<DeleteCachesByKeyResponse, ErrorResponse>

    /**
     * Gets GitHub Actions cache usage for an organization.
     *
     * @param org Organization name.
     * @param token Optional authentication token.
     * @return Organization cache usage statistics or an error response.
     */
    public fun getCacheUsageForOrganization(
        org: String,
        token: String? = null
    ): HttpResponseResult<OrganizationCacheUsageResponse, ErrorResponse>

    /**
     * Lists repositories and their GitHub Actions cache usage for an organization.
     *
     * @param org Organization name.
     * @param perPage Number of results per page (max 100). Default is 30.
     * @param page Page number of results. Default is 1.
     * @param token Optional authentication token.
     * @return Repositories with cache usage or an error response.
     */
    public fun listRepositoriesWithCacheUsageForOrganization(
        org: String,
        perPage: Int = 30,
        page: Int = 1,
        token: String? = null
    ): HttpResponseResult<OrganizationRepositoriesCacheUsageResponse, ErrorResponse>
}

/**
 * Synchronous API interface for GitHub-hosted runners operations.
 */
public interface GithubHostedRunnersApi {

    /**
     * Lists all GitHub-hosted runners configured in an organization.
     *
     * @param org The organization name. The name is not case-sensitive.
     * @param perPage Number of results per page (max 100). Default is 30.
     * @param page Page number of the results to fetch. Default is 1.
     * @param token Optional authentication token.
     * @return HttpResponseResult containing the list of runners or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#list-github-hosted-runners-for-an-organization)
     */
    public suspend fun listHostedRunners(
        org: String,
        perPage: Int = 30,
        page: Int = 1,
        token: String? = null
    ): HttpResponseResult<HostedRunnersResponse, ErrorResponse>

    /**
     * Creates a GitHub-hosted runner for an organization.
     *
     * @param org The organization name. The name is not case-sensitive.
     * @param name Name of the runner. Must be 1-64 characters.
     * @param image Details of the runner image.
     * @param size Machine size of the runner.
     * @param runnerGroupId The existing runner group to add this runner to.
     * @param maximumRunners Optional maximum number of runners to scale up to.
     * @param enableStaticIp Optional flag to enable static public IP.
     * @param token Optional authentication token.
     * @return HttpResponseResult containing the created runner or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#create-a-github-hosted-runner-for-an-organization)
     */
    public suspend fun createHostedRunner(
        org: String,
        name: String,
        image: Map<String, String>,
        size: String,
        runnerGroupId: Long,
        maximumRunners: Int? = null,
        enableStaticIp: Boolean? = null,
        token: String? = null
    ): HttpResponseResult<HostedRunner, ErrorResponse>

    /**
     * Gets GitHub-owned images available for GitHub-hosted runners in an organization.
     *
     * @param org The organization name. The name is not case-sensitive.
     * @param token Optional authentication token.
     * @return HttpResponseResult containing the list of GitHub-owned images or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#get-github-owned-images-for-github-hosted-runners-in-an-organization)
     */
    public suspend fun getGitHubOwnedImages(
        org: String,
        token: String? = null
    ): HttpResponseResult<RunnerImagesResponse, ErrorResponse>

    /**
     * Gets partner images available for GitHub-hosted runners in an organization.
     *
     * @param org The organization name. The name is not case-sensitive.
     * @param token Optional authentication token.
     * @return HttpResponseResult containing the list of partner images or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#get-partner-images-for-github-hosted-runners-in-an-organization)
     */
    public suspend fun getPartnerImages(
        org: String,
        token: String? = null
    ): HttpResponseResult<RunnerImagesResponse, ErrorResponse>

    /**
     * Gets GitHub-hosted runners limits for an organization.
     *
     * @param org The organization name. The name is not case-sensitive.
     * @param token Optional authentication token.
     * @return HttpResponseResult containing the runner limits or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#get-limits-on-github-hosted-runners-for-an-organization)
     */
    public suspend fun getHostedRunnersLimits(
        org: String,
        token: String? = null
    ): HttpResponseResult<RunnerLimitsResponse, ErrorResponse>

    /**
     * Gets machine specs available for GitHub-hosted runners in an organization.
     *
     * @param org The organization name. The name is not case-sensitive.
     * @param token Optional authentication token.
     * @return HttpResponseResult containing the list of machine specs or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#get-github-hosted-runners-machine-specs-for-an-organization)
     */
    public suspend fun getHostedRunnersMachineSpecs(
        org: String,
        token: String? = null
    ): HttpResponseResult<RunnerMachineSpecsResponse, ErrorResponse>

    /**
     * Gets platforms available for GitHub-hosted runners in an organization.
     *
     * @param org The organization name. The name is not case-sensitive.
     * @param token Optional authentication token.
     * @return HttpResponseResult containing the list of platforms or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#get-platforms-for-github-hosted-runners-in-an-organization)
     */
    public suspend fun getHostedRunnersPlatforms(
        org: String,
        token: String? = null
    ): HttpResponseResult<RunnerPlatformsResponse, ErrorResponse>

    /**
     * Gets a specific GitHub-hosted runner in an organization.
     *
     * @param org The organization name. The name is not case-sensitive.
     * @param runnerId Unique identifier of the GitHub-hosted runner.
     * @param token Optional authentication token.
     * @return HttpResponseResult containing the runner details or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#get-a-github-hosted-runner-for-an-organization)
     */
    public suspend fun getHostedRunner(
        org: String,
        runnerId: Long,
        token: String? = null
    ): HttpResponseResult<HostedRunner, ErrorResponse>

    /**
     * Updates a GitHub-hosted runner for an organization.
     *
     * @param org The organization name. The name is not case-sensitive.
     * @param runnerId Unique identifier of the GitHub-hosted runner.
     * @param name Optional new name for the runner.
     * @param runnerGroupId Optional new runner group to add this runner to.
     * @param maximumRunners Optional maximum number of runners to scale up to.
     * @param enableStaticIp Optional flag to update static public IP.
     * @param imageVersion Optional version of the runner image (for custom images).
     * @param token Optional authentication token.
     * @return HttpResponseResult containing the updated runner or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#update-a-github-hosted-runner-for-an-organization)
     */
    public suspend fun updateHostedRunner(
        org: String,
        runnerId: Long,
        name: String? = null,
        runnerGroupId: Long? = null,
        maximumRunners: Int? = null,
        enableStaticIp: Boolean? = null,
        imageVersion: String? = null,
        token: String? = null
    ): HttpResponseResult<HostedRunner, ErrorResponse>

    /**
     * Deletes a GitHub-hosted runner for an organization.
     *
     * @param org The organization name. The name is not case-sensitive.
     * @param runnerId Unique identifier of the GitHub-hosted runner.
     * @param token Optional authentication token.
     * @return HttpResponseResult indicating success or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#delete-a-github-hosted-runner-for-an-organization)
     */
    public suspend fun deleteHostedRunner(
        org: String,
        runnerId: Long,
        token: String? = null
    ): HttpResponseResult<HostedRunner, ErrorResponse>

}

// OIDC
public interface GithubOIDCApi {
}

// Permissions
public interface GithubPermissionsApi {
}

// Secrets
public interface GithubSecretsApi {
}

// Self-hosted runner groups
public interface GithubRunnerGroupsApi {
}

// Variables
public interface GithubVariablesApi {
}

// Workflow jobs
public interface GithubWorkflowJobsApi {
}

// Workflow runs
public interface GithubWorkflowRunsApi {
}

// Workflows
public interface GithubWorkflowsApi {
}