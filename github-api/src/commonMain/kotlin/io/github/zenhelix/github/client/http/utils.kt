package io.github.zenhelix.github.client.http

public object GithubConstants {

    public const val GITHUB_API_PUBLIC_BASE_URL: String = "https://api.github.com"

    public const val APPLICATION_GITHUB_JSON_MEDIA_TYPE: String = "application/vnd.github+json"

    public const val GITHUB_API_VERSION_HEADER_NAME: String = "X-GitHub-Api-Version"

    public const val RATE_LIMIT_LIMIT_HEADER_NAME: String = "X-RateLimit-Limit"
    public const val RATE_LIMIT_REMAINING_HEADER_NAME: String = "X-RateLimit-Remaining"
    public const val RATE_LIMIT_RESET_HEADER_NAME: String = "X-RateLimit-Reset"
    public const val RATE_LIMIT_RESOURCE_HEADER_NAME: String = "X-RateLimit-Resource"
    public const val RATE_LIMIT_USED_HEADER_NAME: String = "X-RateLimit-Used"
}

public enum class GithubApiVersion(public val version: String) {
    V_2022_11_28("2022-11-28")
}
