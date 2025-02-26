package io.github.zenhelix.github.client.http.action

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

public interface GithubActionsCoroutineApi : GithubArtifactCoroutineApi, GithubCacheCoroutineApi, GithubRunnersCoroutineApi

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
 * Asynchronous public interface for GitHub Actions Cache API.
 */
public interface GithubCacheCoroutineApi {

    /**
     * Lists GitHub Actions caches for a repository.
     *
     * @param owner The account owner of the repository. The name is not case-sensitive.
     * @param repository The name of the repository without the .git extension. The name is not case-sensitive.
     * @param key An explicit key or prefix for identifying the cache.
     * @param ref The full Git reference for narrowing down the cache. The `ref` for a branch should be formatted as `refs/heads/<branch name>`. To reference a pull request use `refs/pull/<number>/merge`.
     * @param sort The property to sort the results by. Default: "last_accessed_at".
     *             Must be one of: "created_at", "last_accessed_at", "size_in_bytes".
     * @param direction The direction to sort the results by. Default: "desc".
     *                  Must be one of: "asc", "desc".
     * @param perPage The number of results per page (max 100). Default: 30
     * @param page Page number of the results to fetch. Default: 1
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the list of caches or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/cache?apiVersion=2022-11-28#list-github-actions-caches-for-a-repository)
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
     * @param owner The account owner of the repository. The name is not case-sensitive.
     * @param repository The name of the repository without the .git extension. The name is not case-sensitive.
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the cache usage statistics or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/cache?apiVersion=2022-11-28#get-github-actions-cache-usage-for-a-repository)
     */
    public suspend fun getCacheUsageForRepository(
        owner: String,
        repository: String,
        token: String? = null
    ): HttpResponseResult<CacheUsageResponse, ErrorResponse>

    /**
     * Deletes a GitHub Actions cache for a repository using a cache ID.
     *
     * @param owner The account owner of the repository. The name is not case-sensitive.
     * @param repository The name of the repository without the .git extension. The name is not case-sensitive.
     * @param cacheId The unique identifier of the cache to delete.
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] indicating success or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/cache?apiVersion=2022-11-28#delete-a-github-actions-cache-for-a-repository-using-a-cache-id)
     */
    public suspend fun deleteCache(
        owner: String,
        repository: String,
        cacheId: Long,
        token: String? = null
    ): HttpResponseResult<Unit, ErrorResponse>

    /**
     * Deletes GitHub Actions caches for a repository using a key.
     *
     * @param owner The account owner of the repository. The name is not case-sensitive.
     * @param repository The name of the repository without the .git extension. The name is not case-sensitive.
     * @param key A key identifying the cache to delete. All caches that match the key will be deleted.
     * @param ref The full Git reference for narrowing down the cache. If provided, only caches that match both the key and ref will be deleted.
     *           The `ref` for a branch should be formatted as `refs/heads/<branch name>`.
     *           To reference a pull request use `refs/pull/<number>/merge`.
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing details about the deleted caches or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/cache?apiVersion=2022-11-28#delete-github-actions-caches-for-a-repository-using-a-cache-key)
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
     * @param org The organization name. The name is not case sensitive.
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the organization cache usage statistics or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/cache?apiVersion=2022-11-28#get-github-actions-cache-usage-for-an-organization)
     */
    public suspend fun getCacheUsageForOrganization(
        org: String,
        token: String? = null
    ): HttpResponseResult<OrganizationCacheUsageResponse, ErrorResponse>

    /**
     * Lists repositories and their GitHub Actions cache usage for an organization.
     *
     * @param org The organization name. The name is not case sensitive.
     * @param perPage The number of results per page (max 100). Default: 30
     * @param page Page number of the results to fetch. Default: 1
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the repositories with cache usage or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/cache?apiVersion=2022-11-28#list-repositories-with-github-actions-cache-usage-for-an-organization)
     */
    public suspend fun listRepositoriesWithCacheUsageForOrganization(
        org: String,
        perPage: Int = 30,
        page: Int = 1,
        token: String? = null
    ): HttpResponseResult<OrganizationRepositoriesCacheUsageResponse, ErrorResponse>

}

public interface GithubRunnersCoroutineApi