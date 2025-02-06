package io.github.zenhelix.github.client.http

import io.github.zenhelix.github.client.http.model.LicensesResponse

public interface GithubCoroutineApi : GithubActionsCoroutineApi, GithubLicensesCoroutineApi

public interface GithubLicensesCoroutineApi {

    public suspend fun licenses(token: String? = null): LicensesResponse

}

public interface GithubActionsCoroutineApi {

    /**
     * Lists all artifacts for a repository.
     *
     * @param owner The account owner of the repository. The name is not case-sensitive.
     * @param repository The name of the repository without the .git extension. The name is not case-sensitive.
     *
     * [doc](https://docs.github.com/en/rest/actions/artifacts?apiVersion=2022-11-28#list-artifacts-for-a-repository)
     */
    public suspend fun artifacts(owner: String, repository: String)

}