package io.github.zenhelix.github.client.http

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

public interface GithubCoroutineApi : GithubActionsCoroutineApi, GithubActivityCoroutineApi,
                                      GithubAppsCoroutineApi, GithubBillingCoroutineApi, GithubBranchesCoroutineApi,
                                      GithubChecksCoroutineApi, GithubClassroomCoroutineApi,
                                      GithubCodeScanningCoroutineApi, GithubCodeSecurityCoroutineApi, GithubCodesOfConductCoroutineApi,
                                      GithubCodespacesCoroutineApi, GithubCollaboratorsCoroutineApi, GithubCommitsCoroutineApi,
                                      GithubCopilotCoroutineApi, GithubDependabotCoroutineApi, GithubDependencyGraphCoroutineApi,
                                      GithubDeployKeysCoroutineApi, GithubDeploymentsCoroutineApi, GithubEmojisCoroutineApi,
                                      GithubGistsCoroutineApi, GithubGitCoroutineApi, GithubGitignoreCoroutineApi, GithubInteractionsCoroutineApi,
                                      GithubIssuesCoroutineApi, GithubLicensesCoroutineApi, GithubMarkdownCoroutineApi, GithubMetaCoroutineApi,
                                      GithubMetricsCoroutineApi, GithubMigrationsCoroutineApi, GithubOrganizationsCoroutineApi, GithubPackagesCoroutineApi,
                                      GithubPrivateRegistriesCoroutineApi, GithubProjectCoroutineApi, GithubPullRequestsCoroutineApi,
                                      GithubRateLimitCoroutineApi, GithubReactionsCoroutineApi, GithubReleasesCoroutineApi, GithubRepositoriesCoroutineApi,
                                      GithubSearchCoroutineApi, GithubSecretScanningCoroutineApi, GithubSecurityAdvisoriesCoroutineApi, GithubTeamsCoroutineApi,
                                      GithubUsersCoroutineApi

public interface GithubLicensesCoroutineApi {

    public suspend fun licenses(token: String? = null): HttpResponseResult<LicensesResponse, ErrorResponse>

}

public interface GithubActionsCoroutineApi : GithubArtifactCoroutineApi, GithubCacheCoroutineApi, GithubRunnersCoroutineApi
public interface GithubActivityCoroutineApi
public interface GithubAppsCoroutineApi
public interface GithubBillingCoroutineApi
public interface GithubBranchesCoroutineApi
public interface GithubChecksCoroutineApi
public interface GithubClassroomCoroutineApi
public interface GithubCodeScanningCoroutineApi
public interface GithubCodeSecurityCoroutineApi
public interface GithubCodesOfConductCoroutineApi
public interface GithubCodespacesCoroutineApi
public interface GithubCollaboratorsCoroutineApi
public interface GithubCommitsCoroutineApi
public interface GithubCopilotCoroutineApi
public interface GithubDependabotCoroutineApi
public interface GithubDependencyGraphCoroutineApi
public interface GithubDeployKeysCoroutineApi
public interface GithubDeploymentsCoroutineApi
public interface GithubEmojisCoroutineApi
public interface GithubGistsCoroutineApi
public interface GithubGitCoroutineApi
public interface GithubGitignoreCoroutineApi
public interface GithubInteractionsCoroutineApi
public interface GithubIssuesCoroutineApi
public interface GithubMarkdownCoroutineApi
public interface GithubMetaCoroutineApi
public interface GithubMetricsCoroutineApi
public interface GithubMigrationsCoroutineApi
public interface GithubOrganizationsCoroutineApi
public interface GithubPackagesCoroutineApi
public interface GithubPagesCoroutineApi
public interface GithubPrivateRegistriesCoroutineApi
public interface GithubProjectCoroutineApi
public interface GithubPullRequestsCoroutineApi
public interface GithubRateLimitCoroutineApi
public interface GithubReactionsCoroutineApi
public interface GithubReleasesCoroutineApi
public interface GithubRepositoriesCoroutineApi
public interface GithubSearchCoroutineApi
public interface GithubSecretScanningCoroutineApi
public interface GithubSecurityAdvisoriesCoroutineApi
public interface GithubTeamsCoroutineApi
public interface GithubUsersCoroutineApi

public interface GithubArtifactCoroutineApi {

    /**
     * Lists all artifacts for a repository.
     *
     * @param owner The account owner of the repository. The name is not case-sensitive.
     * @param repository The name of the repository without the .git extension. The name is not case-sensitive.
     * @param perPage The number of results per page (max 100). Default: 30
     * @param page Page number of the results to fetch. Default: 1
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the list of artifacts or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/artifacts?apiVersion=2022-11-28#list-artifacts-for-a-repository)
     */
    public suspend fun artifacts(
        owner: String,
        repository: String,
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
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the list of artifacts or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/artifacts?apiVersion=2022-11-28#list-workflow-run-artifacts)
     */
    public suspend fun workflowRunArtifacts(
        owner: String,
        repository: String,
        runId: Long,
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
 * Asynchronous interface for GitHub Actions Cache API.
 */
public interface GithubCacheCoroutineApi {

    /**
     * Lists GitHub Actions caches for a repository.
     *
     * @param owner The account owner of the repository. The name is not case-sensitive.
     * @param repository The name of the repository without the .git extension. The name is not case-sensitive.
     * @param key The full or partial cache key to filter results.
     * @param ref The full Git reference to filter results. The ref can be a branch, tag, or a commit SHA.
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
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] indicating success or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/cache?apiVersion=2022-11-28#delete-github-actions-caches-for-a-repository-using-a-cache-key)
     */
    public suspend fun deleteCachesByKey(
        owner: String,
        repository: String,
        key: String,
        token: String? = null
    ): HttpResponseResult<DeleteCachesByKeyResponse, ErrorResponse>

}

public interface GithubRepositoryRunnersCoroutineApi {

    /**
     * Lists self-hosted runners for a repository.
     *
     * @param owner The account owner of the repository. The name is not case-sensitive.
     * @param repository The name of the repository without the .git extension. The name is not case-sensitive.
     * @param perPage The number of results per page (max 100). Default: 30
     * @param page Page number of the results to fetch. Default: 1
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the list of runners or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#list-self-hosted-runners-for-a-repository)
     */
    public suspend fun listRunnersForRepository(
        owner: String,
        repository: String,
        perPage: Int = 30,
        page: Int = 1,
        token: String? = null
    ): HttpResponseResult<RunnersResponse, ErrorResponse>

    /**
     * Gets a self-hosted runner for a repository.
     *
     * @param owner The account owner of the repository. The name is not case-sensitive.
     * @param repository The name of the repository without the .git extension. The name is not case-sensitive.
     * @param runnerId The ID of the runner.
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the runner details or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#get-a-self-hosted-runner-for-a-repository)
     */
    public suspend fun getRunnerForRepository(
        owner: String,
        repository: String,
        runnerId: Long,
        token: String? = null
    ): HttpResponseResult<Runner, ErrorResponse>

    /**
     * Deletes a self-hosted runner from a repository.
     *
     * @param owner The account owner of the repository. The name is not case-sensitive.
     * @param repository The name of the repository without the .git extension. The name is not case-sensitive.
     * @param runnerId The ID of the runner to delete.
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] indicating success or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#delete-a-self-hosted-runner-from-a-repository)
     */
    public suspend fun deleteRunnerFromRepository(
        owner: String,
        repository: String,
        runnerId: Long,
        token: String? = null
    ): HttpResponseResult<Unit, ErrorResponse>

    /**
     * Creates a registration token for a repository.
     *
     * @param owner The account owner of the repository. The name is not case-sensitive.
     * @param repository The name of the repository without the .git extension. The name is not case-sensitive.
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the registration token or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#create-a-registration-token-for-a-repository)
     */
    public suspend fun createRunnerRegistrationTokenForRepository(
        owner: String,
        repository: String,
        token: String? = null
    ): HttpResponseResult<RunnerRegistrationToken, ErrorResponse>

    /**
     * Creates a remove token for a repository.
     *
     * @param owner The account owner of the repository. The name is not case-sensitive.
     * @param repository The name of the repository without the .git extension. The name is not case-sensitive.
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the remove token or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#create-a-remove-token-for-a-repository)
     */
    public suspend fun createRunnerRemoveTokenForRepository(
        owner: String,
        repository: String,
        token: String? = null
    ): HttpResponseResult<RunnerRemoveToken, ErrorResponse>

    /**
     * Lists runner applications that can be downloaded and configured for a repository self-hosted runner.
     *
     * @param owner The account owner of the repository. The name is not case-sensitive.
     * @param repository The name of the repository without the .git extension. The name is not case-sensitive.
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the list of runner applications or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#list-runner-applications-for-a-repository)
     */
    public suspend fun listRunnerApplicationsForRepository(
        owner: String,
        repository: String,
        token: String? = null
    ): HttpResponseResult<RunnerApplicationsResponse, ErrorResponse>

}

public interface GithubOrganizationRunnersCoroutineApi {
    /**
     * Lists self-hosted runners for an organization.
     *
     * @param org The organization name. The name is not case-sensitive.
     * @param perPage The number of results per page (max 100). Default: 30
     * @param page Page number of the results to fetch. Default: 1
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the list of runners or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#list-self-hosted-runners-for-an-organization)
     */
    public suspend fun listRunnersForOrganization(
        org: String,
        perPage: Int = 30,
        page: Int = 1,
        token: String? = null
    ): HttpResponseResult<RunnersResponse, ErrorResponse>

    /**
     * Gets a self-hosted runner for an organization.
     *
     * @param org The organization name. The name is not case-sensitive.
     * @param runnerId The ID of the runner.
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the runner details or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#get-a-self-hosted-runner-for-an-organization)
     */
    public suspend fun getRunnerForOrganization(
        org: String,
        runnerId: Long,
        token: String? = null
    ): HttpResponseResult<Runner, ErrorResponse>

    /**
     * Deletes a self-hosted runner from an organization.
     *
     * @param org The organization name. The name is not case-sensitive.
     * @param runnerId The ID of the runner to delete.
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] indicating success or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#delete-a-self-hosted-runner-from-an-organization)
     */
    public suspend fun deleteRunnerFromOrganization(
        org: String,
        runnerId: Long,
        token: String? = null
    ): HttpResponseResult<Unit, ErrorResponse>

    /**
     * Creates a registration token for an organization.
     *
     * @param org The organization name. The name is not case-sensitive.
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the registration token or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#create-a-registration-token-for-an-organization)
     */
    public suspend fun createRunnerRegistrationTokenForOrganization(
        org: String,
        token: String? = null
    ): HttpResponseResult<RunnerRegistrationToken, ErrorResponse>

    /**
     * Creates a remove token for an organization.
     *
     * @param org The organization name. The name is not case-sensitive.
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the remove token or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#create-a-remove-token-for-an-organization)
     */
    public suspend fun createRunnerRemoveTokenForOrganization(
        org: String,
        token: String? = null
    ): HttpResponseResult<RunnerRemoveToken, ErrorResponse>

    /**
     * Lists runner applications that can be downloaded and configured for an organization self-hosted runner.
     *
     * @param org The organization name. The name is not case-sensitive.
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the list of runner applications or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#list-runner-applications-for-an-organization)
     */
    public suspend fun listRunnerApplicationsForOrganization(
        org: String,
        token: String? = null
    ): HttpResponseResult<RunnerApplicationsResponse, ErrorResponse>
}

public interface GithubEnterpriseRunnersCoroutineApi {
    /**
     * Lists self-hosted runners for an enterprise.
     *
     * @param enterprise The slug version of the enterprise name.
     * @param perPage The number of results per page (max 100). Default: 30
     * @param page Page number of the results to fetch. Default: 1
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the list of runners or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#list-self-hosted-runners-for-an-enterprise)
     */
    public suspend fun listRunnersForEnterprise(
        enterprise: String,
        perPage: Int = 30,
        page: Int = 1,
        token: String? = null
    ): HttpResponseResult<RunnersResponse, ErrorResponse>

    /**
     * Gets a self-hosted runner for an enterprise.
     *
     * @param enterprise The slug version of the enterprise name.
     * @param runnerId The ID of the runner.
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the runner details or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#get-a-self-hosted-runner-for-an-enterprise)
     */
    public suspend fun getRunnerForEnterprise(
        enterprise: String,
        runnerId: Long,
        token: String? = null
    ): HttpResponseResult<Runner, ErrorResponse>

    /**
     * Deletes a self-hosted runner from an enterprise.
     *
     * @param enterprise The slug version of the enterprise name.
     * @param runnerId The ID of the runner to delete.
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] indicating success or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#delete-a-self-hosted-runner-from-an-enterprise)
     */
    public suspend fun deleteRunnerFromEnterprise(
        enterprise: String,
        runnerId: Long,
        token: String? = null
    ): HttpResponseResult<Unit, ErrorResponse>

    /**
     * Creates a registration token for an enterprise.
     *
     * @param enterprise The slug version of the enterprise name.
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the registration token or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#create-a-registration-token-for-an-enterprise)
     */
    public suspend fun createRunnerRegistrationTokenForEnterprise(
        enterprise: String,
        token: String? = null
    ): HttpResponseResult<RunnerRegistrationToken, ErrorResponse>

    /**
     * Creates a remove token for an enterprise.
     *
     * @param enterprise The slug version of the enterprise name.
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the remove token or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#create-a-remove-token-for-an-enterprise)
     */
    public suspend fun createRunnerRemoveTokenForEnterprise(
        enterprise: String,
        token: String? = null
    ): HttpResponseResult<RunnerRemoveToken, ErrorResponse>

    /**
     * Lists runner applications that can be downloaded and configured for an enterprise self-hosted runner.
     *
     * @param enterprise The slug version of the enterprise name.
     * @param token The GitHub personal access token for authentication. If null, the default token will be used.
     * @return [HttpResponseResult] containing the list of runner applications or an error.
     *
     * [API Documentation](https://docs.github.com/en/rest/actions/hosted-runners?apiVersion=2022-11-28#list-runner-applications-for-an-enterprise)
     */
    public suspend fun listRunnerApplicationsForEnterprise(
        enterprise: String,
        token: String? = null
    ): HttpResponseResult<RunnerApplicationsResponse, ErrorResponse>
}

/**
 * Asynchronous interface for GitHub Actions Self-Hosted Runners API.
 */
public interface GithubRunnersCoroutineApi : GithubRepositoryRunnersCoroutineApi, GithubOrganizationRunnersCoroutineApi, GithubEnterpriseRunnersCoroutineApi