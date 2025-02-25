package io.github.zenhelix.github.client.http.webflux.utils

import io.github.zenhelix.github.client.http.GithubApiVersion
import io.github.zenhelix.github.client.http.GithubConstants.APPLICATION_GITHUB_JSON_MEDIA_TYPE
import io.github.zenhelix.github.client.http.GithubConstants.GITHUB_API_VERSION_HEADER_NAME
import io.github.zenhelix.github.client.http.webflux.utils.MediaTypes.APPLICATION_GITHUB_JSON
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

public object MediaTypes {
    public val APPLICATION_GITHUB_JSON: MediaType = MediaType.parseMediaType(APPLICATION_GITHUB_JSON_MEDIA_TYPE)
}

internal fun HttpHeaders.acceptGithubJson() = apply {
    this.accept = listOf(APPLICATION_GITHUB_JSON, MediaType.APPLICATION_JSON)
}

internal fun HttpHeaders.githubVersion(version: GithubApiVersion = GithubApiVersion.V_2022_11_28) = apply {
    this.set(GITHUB_API_VERSION_HEADER_NAME, version.version)
}