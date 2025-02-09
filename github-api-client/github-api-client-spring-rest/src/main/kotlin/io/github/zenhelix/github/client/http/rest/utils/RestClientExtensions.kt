package io.github.zenhelix.github.client.http.rest.utils

import io.github.zenhelix.github.client.http.GithubApiVersion
import io.github.zenhelix.github.client.http.GithubConstants.GITHUB_API_VERSION_HEADER_NAME
import io.github.zenhelix.github.client.http.model.HttpResponseResult
import io.github.zenhelix.github.client.http.rest.utils.MediaTypes.APPLICATION_GITHUB_JSON
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient

internal fun <T : RestClient.RequestHeadersSpec<T>> RestClient.RequestHeadersSpec<T>.acceptGithubJson(): T =
    this.accept(APPLICATION_GITHUB_JSON, MediaType.APPLICATION_JSON)

internal fun <T : RestClient.RequestHeadersSpec<T>> RestClient.RequestHeadersSpec<T>.bearer(token: String): T =
    this.header(HttpHeaders.AUTHORIZATION, "Bearer $token")

internal fun <T : RestClient.RequestHeadersSpec<T>> RestClient.RequestHeadersSpec<T>.apiVersion(version: GithubApiVersion = GithubApiVersion.V_2022_11_28): T =
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

public inline fun <reified S : Any, reified E : Any> RestClient.RequestHeadersSpec<out RestClient.RequestHeadersSpec<*>>.result(
    vararg successStatuses: HttpStatus = successHttpStatuses.toTypedArray()
): HttpResponseResult<S, E> = try {
    this.exchange { req, clientResponse ->
        val status = clientResponse.statusCode
        val headers = clientResponse.headers.toMap()

        when {
            status in successStatuses -> try {
                HttpResponseResult.Success(
                    data = clientResponse.bodyTo(object : ParameterizedTypeReference<S>() {}),
                    httpStatus = status.value(),
                    httpHeaders = headers
                )
            } catch (e: Exception) {
                HttpResponseResult.UnexpectedError(
                    cause = e,
                    httpStatus = status.value(),
                    httpHeaders = headers
                )
            }

            else                      -> try {
                HttpResponseResult.Error(
                    data = clientResponse.bodyTo(object : ParameterizedTypeReference<E>() {}),
                    cause = null,
                    httpStatus = status.value(),
                    httpHeaders = headers
                )
            } catch (e: Exception) {
                HttpResponseResult.UnexpectedError(
                    cause = e,
                    httpStatus = status.value(),
                    httpHeaders = headers
                )
            }
        }
    }!!
} catch (e: Exception) {
    HttpResponseResult.UnexpectedError(
        cause = e,
        httpStatus = null,
        httpHeaders = null
    )
}