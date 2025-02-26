package io.github.zenhelix.github.client.http.api.action

import io.github.zenhelix.github.client.http.model.ErrorResponse
import io.github.zenhelix.github.client.http.model.HttpResponseResult
import io.github.zenhelix.github.client.http.model.action.ArtifactResponse
import io.github.zenhelix.github.client.http.model.action.ArtifactsResponse
import io.github.zenhelix.github.client.http.model.action.CacheListResponse
import io.github.zenhelix.github.client.http.model.action.CacheUsageResponse
import io.github.zenhelix.github.client.http.model.action.DeleteCachesByKeyResponse
import io.github.zenhelix.github.client.http.model.action.OrganizationCacheUsageResponse
import io.github.zenhelix.github.client.http.model.action.OrganizationRepositoriesCacheUsageResponse
import io.github.zenhelix.github.client.http.model.action.WorkflowRunArtifactsResponse

public interface GithubActionsCoroutineApi : GithubArtifactCoroutineApi, GithubCacheCoroutineApi, GithubGitHubHostedRunnersCoroutineApi, GithubOIDCCoroutineApi,
                                             GithubPermissionsCoroutineApi, GithubSecretsCoroutineApi, GithubRunnerGroupsCoroutineApi,
                                             GithubVariablesCoroutineApi, GithubWorkflowJobsCoroutineApi, GithubWorkflowRunsCoroutineApi,
                                             GithubWorkflowsCoroutineApi

public interface GithubArtifactCoroutineApi {

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
    public suspend fun artifacts(
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
    public suspend fun workflowRunArtifacts(
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
    public suspend fun artifact(
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
    public suspend fun deleteArtifact(
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
    public suspend fun downloadArtifact(
        owner: String,
        repository: String,
        artifactId: Long,
        archiveFormat: String = "zip",
        token: String? = null
    ): HttpResponseResult<ByteArray, ErrorResponse>

}

/**
 * Asynchronous interface for GitHub Actions Cache API operations.
 */
public interface GithubCacheCoroutineApi {

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
    public suspend fun listCachesForRepository(
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
    public suspend fun getCacheUsageForRepository(
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
    public suspend fun deleteCache(
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
    public suspend fun deleteCachesByKey(
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
    public suspend fun getCacheUsageForOrganization(
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
    public suspend fun listRepositoriesWithCacheUsageForOrganization(
        org: String,
        perPage: Int = 30,
        page: Int = 1,
        token: String? = null
    ): HttpResponseResult<OrganizationRepositoriesCacheUsageResponse, ErrorResponse>
}

// GitHub-hosted runners
public interface GithubGitHubHostedRunnersCoroutineApi {
}

// OIDC
public interface GithubOIDCCoroutineApi {
}

// Permissions
public interface GithubPermissionsCoroutineApi {
}

// Secrets
public interface GithubSecretsCoroutineApi {
}

// Self-hosted runner groups
public interface GithubRunnerGroupsCoroutineApi {
}

// Variables
public interface GithubVariablesCoroutineApi {
}

// Workflow jobs
public interface GithubWorkflowJobsCoroutineApi {
}

// Workflow runs
public interface GithubWorkflowRunsCoroutineApi {
}

// Workflows
public interface GithubWorkflowsCoroutineApi {
}