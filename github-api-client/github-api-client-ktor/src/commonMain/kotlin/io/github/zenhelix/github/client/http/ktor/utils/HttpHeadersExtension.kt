package io.github.zenhelix.github.client.http.ktor.utils

import io.github.zenhelix.github.client.http.GithubApiVersion
import io.github.zenhelix.github.client.http.GithubConstants.APPLICATION_GITHUB_JSON_MEDIA_TYPE
import io.github.zenhelix.github.client.http.GithubConstants.GITHUB_API_VERSION_HEADER_NAME
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessageBuilder

public val HttpHeaders.GithubApiVersion: String
    get() = GITHUB_API_VERSION_HEADER_NAME

internal fun HttpMessageBuilder.githubApiVersion(version: GithubApiVersion = GithubApiVersion.V_2022_11_28): Unit =
    header(HttpHeaders.GithubApiVersion, version.version)

internal fun HttpMessageBuilder.acceptGithubJson(): Unit =
    header(HttpHeaders.Accept, APPLICATION_GITHUB_JSON_MEDIA_TYPE)