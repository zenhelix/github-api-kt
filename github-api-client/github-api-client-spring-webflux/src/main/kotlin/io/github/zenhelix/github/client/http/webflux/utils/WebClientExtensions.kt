package io.github.zenhelix.github.client.http.webflux.utils

import io.github.zenhelix.github.client.http.GithubApiVersion
import io.github.zenhelix.github.client.http.GithubConstants.GITHUB_API_VERSION_HEADER_NAME
import io.github.zenhelix.github.client.http.webflux.utils.MediaTypes.APPLICATION_GITHUB_JSON
import kotlinx.serialization.json.Json
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.http.codec.json.KotlinSerializationJsonEncoder
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClient.Builder

internal fun Builder.kotlinSerializationJsonCodec(mapper: Json = Json.Default): Builder = this.codecs {
    it.defaultCodecs().apply {
        this.kotlinSerializationJsonDecoder(KotlinSerializationJsonDecoder(mapper))
        this.kotlinSerializationJsonEncoder(KotlinSerializationJsonEncoder(mapper))
    }
}

internal fun <T : WebClient.RequestHeadersSpec<T>> WebClient.RequestHeadersSpec<T>.acceptGithubJson(): T =
    this.accept(APPLICATION_GITHUB_JSON, MediaType.APPLICATION_JSON)

internal fun <T : WebClient.RequestHeadersSpec<T>> WebClient.RequestHeadersSpec<T>.bearer(token: String): T =
    this.header(HttpHeaders.AUTHORIZATION, "Bearer $token")

internal fun <T : WebClient.RequestHeadersSpec<T>> WebClient.RequestHeadersSpec<T>.apiVersion(version: GithubApiVersion = GithubApiVersion.V_2022_11_28): T =
    this.header(GITHUB_API_VERSION_HEADER_NAME, version.version)

