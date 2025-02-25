package io.github.zenhelix.github.client.http.ktor.utils

import io.github.zenhelix.github.client.http.model.HttpResponseResult
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.Accepted
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.MultiStatus
import io.ktor.http.HttpStatusCode.Companion.NoContent
import io.ktor.http.HttpStatusCode.Companion.NonAuthoritativeInformation
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.HttpStatusCode.Companion.PartialContent
import io.ktor.http.HttpStatusCode.Companion.ResetContent
import io.ktor.util.toMap

public val successHttpCodes: Set<HttpStatusCode> = setOf(
    OK, Created, Accepted,
    NonAuthoritativeInformation,
    NoContent, ResetContent, PartialContent, MultiStatus
)

public object HttpClientExtensions {

    public suspend inline fun <reified S : Any, reified E : Any> HttpResponse.result(
        vararg successStatuses: HttpStatusCode = successHttpCodes.toTypedArray()
    ): HttpResponseResult<S, E> = try {
        when (this.status) {
            in successStatuses -> try {
                HttpResponseResult.Success(
                    data = this.body<S>(),
                    httpStatus = this.status.value,
                    httpHeaders = this.headers.toMap()
                )
            } catch (e: Exception) {
                HttpResponseResult.UnexpectedError(
                    cause = e,
                    httpStatus = this.status.value,
                    httpHeaders = this.headers.toMap()
                )
            }

            else               -> try {
                HttpResponseResult.Error(
                    data = this.body<E>(),
                    cause = null,
                    httpStatus = this.status.value,
                    httpHeaders = this.headers.toMap()
                )
            } catch (e: Exception) {
                HttpResponseResult.UnexpectedError(
                    cause = e,
                    httpStatus = this.status.value,
                    httpHeaders = this.headers.toMap()
                )
            }
        }
    } catch (e: Exception) {
        when (e) {
            is ResponseException -> try {
                HttpResponseResult.Error(
                    data = e.response.body<E>(),
                    cause = e,
                    httpStatus = e.response.status.value,
                    httpHeaders = e.response.headers.toMap()
                )
            } catch (subException: Exception) {
                HttpResponseResult.UnexpectedError(
                    cause = subException,
                    httpStatus = e.response.status.value,
                    httpHeaders = e.response.headers.toMap()
                )
            }

            else                 -> HttpResponseResult.UnexpectedError(e)
        }
    }

}