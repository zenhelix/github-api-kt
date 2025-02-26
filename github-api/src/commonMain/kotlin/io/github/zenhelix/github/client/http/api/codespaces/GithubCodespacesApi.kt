package io.github.zenhelix.github.client.http.api.codespaces

public interface GithubCodespacesApi :
    GithubBasicCodespacesApi,
    GithubCodespacesOrganizationsApi,
    GithubCodespacesOrganizationSecretsApi,
    GithubCodespacesMachinesApi,
    GithubCodespacesRepositorySecretsApi,
    GithubCodespacesUserSecretsApi

public interface GithubBasicCodespacesApi {
}

public interface GithubCodespacesOrganizationsApi {
}

public interface GithubCodespacesOrganizationSecretsApi {
}

public interface GithubCodespacesMachinesApi {
}

public interface GithubCodespacesRepositorySecretsApi {
}

public interface GithubCodespacesUserSecretsApi {
}