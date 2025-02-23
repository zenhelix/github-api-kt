package io.github.zenhelix.github.client.http.webflux.utils

import io.github.zenhelix.github.client.http.GithubApiVersion
import io.github.zenhelix.github.client.http.GithubConstants.GITHUB_API_VERSION_HEADER_NAME
import io.github.zenhelix.github.client.http.model.HttpResponseResult
import io.github.zenhelix.github.client.http.webflux.utils.MediaTypes.APPLICATION_GITHUB_JSON
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.serialization.json.Json
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.http.codec.json.KotlinSerializationJsonEncoder
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClient.Builder
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.awaitBodilessEntity
import org.springframework.web.reactive.function.client.awaitExchange
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

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

public val successHttpStatuses: Set<HttpStatus> = setOf(
    HttpStatus.OK,
    HttpStatus.CREATED,
    HttpStatus.ACCEPTED,
    HttpStatus.NON_AUTHORITATIVE_INFORMATION,
    HttpStatus.NO_CONTENT,
    HttpStatus.RESET_CONTENT,
    HttpStatus.PARTIAL_CONTENT,
    HttpStatus.MULTI_STATUS
)

public inline fun <reified S : Any, reified E : Any> WebClient.RequestHeadersSpec<*>.result(
    vararg successStatuses: HttpStatus = successHttpStatuses.toTypedArray()
): Mono<HttpResponseResult<S, E>> {
    return this.retrieve()
        .onStatus(
            { status -> status !in successStatuses },
            { response -> response.createException() }
        )
        .toEntity(object : ParameterizedTypeReference<S>() {})
        .map { data ->
            HttpResponseResult.Success(
                data = data.body,
                httpStatus = data.statusCode.value(),
                httpHeaders = data.headers
            ) as HttpResponseResult<S, E>
        }
        .onErrorResume { ex ->
            when (ex) {
                is WebClientResponseException -> {
                    try {
                        HttpResponseResult.Error<E>(
                            data = ex.getResponseBodyAs(object : ParameterizedTypeReference<E>() {}),
                            cause = ex,
                            httpStatus = ex.statusCode.value(),
                            httpHeaders = ex.headers.toMap()
                        ).let { Mono.just(it) }
                    } catch (parseEx: Exception) {
                        HttpResponseResult.UnexpectedError(
                            cause = parseEx,
                            httpStatus = ex.statusCode.value(),
                            httpHeaders = ex.headers.toMap()
                        ).let { Mono.just(it) }
                    }
                }

                else                          ->
                    HttpResponseResult.UnexpectedError(
                        cause = ex,
                        httpStatus = null,
                        httpHeaders = null
                    ).let { Mono.just(it) }
            }
        }
}

public suspend inline fun <reified T : Any, reified E : Any> WebClient.RequestHeadersSpec<out WebClient.RequestHeadersSpec<*>>.awaitResult(
    vararg successStatuses: HttpStatus = successHttpStatuses.toTypedArray()
): HttpResponseResult<T, E> = try {
    awaitExchange { clientResponse ->
        try {
            if (clientResponse.statusCode() in successStatuses) {
                HttpResponseResult.Success(
                    data = clientResponse.awaitBodyOrBodiless(),
                    httpStatus = clientResponse.statusCode().value(),
                    httpHeaders = clientResponse.headers().asHttpHeaders()
                )
            } else {
                HttpResponseResult.Error(
                    data = clientResponse.awaitBodyOrBodiless(),
                    httpStatus = clientResponse.statusCode().value(),
                    httpHeaders = clientResponse.headers().asHttpHeaders()
                )
            }
        } catch (e: Exception) {
            HttpResponseResult.UnexpectedError(
                cause = e,
                httpStatus = clientResponse.statusCode().value(),
                httpHeaders = clientResponse.headers().asHttpHeaders()
            )
        }
    }
} catch (e: Exception) {
    HttpResponseResult.UnexpectedError(cause = e)
}

public suspend inline fun <reified T : Any> ClientResponse.awaitBodyOrBodiless(): T = when (T::class) {
    Unit::class -> awaitBodilessEntity().let { Unit as T }
    else        -> bodyToMono<T>().awaitSingle()
}