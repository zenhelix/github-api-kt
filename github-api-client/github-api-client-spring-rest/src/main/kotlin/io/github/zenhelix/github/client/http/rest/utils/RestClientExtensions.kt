package io.github.zenhelix.github.client.http.rest.utils

import io.github.zenhelix.github.client.http.GithubApiVersion
import io.github.zenhelix.github.client.http.GithubConstants.GITHUB_API_VERSION_HEADER_NAME
import io.github.zenhelix.github.client.http.rest.utils.MediaTypes.APPLICATION_GITHUB_JSON
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient

internal fun <T : RestClient.RequestHeadersSpec<T>> RestClient.RequestHeadersSpec<T>.acceptGithubJson(): T =
    this.accept(APPLICATION_GITHUB_JSON, MediaType.APPLICATION_JSON)

internal fun <T : RestClient.RequestHeadersSpec<T>> RestClient.RequestHeadersSpec<T>.bearer(token: String): T =
    this.header(HttpHeaders.AUTHORIZATION, "Bearer $token")

internal fun <T : RestClient.RequestHeadersSpec<T>> RestClient.RequestHeadersSpec<T>.apiVersion(version: GithubApiVersion = GithubApiVersion.V_2022_11_28): T =
    this.header(GITHUB_API_VERSION_HEADER_NAME, version.version)
