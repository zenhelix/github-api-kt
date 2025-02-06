package io.github.zenhelix.github.client.http.ktor.utils

import io.github.zenhelix.github.client.http.GithubConstants.GITHUB_API_VERSION_HEADER_NAME
import io.ktor.http.HttpHeaders

public val HttpHeaders.GithubApiVersion: String
    get() = GITHUB_API_VERSION_HEADER_NAME
