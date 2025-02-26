package io.github.zenhelix.github.client.http.api.codespaces

public interface GithubCodespacesCoroutineApi :
    GithubBasicCodespacesCoroutineApi,
    GithubCodespacesOrganizationsCoroutineApi,
    GithubCodespacesOrganizationSecretsCoroutineApi,
    GithubCodespacesMachinesCoroutineApi,
    GithubCodespacesRepositorySecretsCoroutineApi,
    GithubCodespacesUserSecretsCoroutineApi

public interface GithubBasicCodespacesCoroutineApi {
}

public interface GithubCodespacesOrganizationsCoroutineApi {
}

public interface GithubCodespacesOrganizationSecretsCoroutineApi {
}

public interface GithubCodespacesMachinesCoroutineApi {
}

public interface GithubCodespacesRepositorySecretsCoroutineApi {
}

public interface GithubCodespacesUserSecretsCoroutineApi {
}