package io.github.zenhelix.github.client.http.ktor

import io.github.zenhelix.github.client.http.GithubConstants.GITHUB_API_PUBLIC_BASE_URL
import io.github.zenhelix.github.client.http.GithubCoroutineApi
import io.github.zenhelix.github.client.http.ktor.utils.HttpClientExtensions.result
import io.github.zenhelix.github.client.http.ktor.utils.acceptGithubJson
import io.github.zenhelix.github.client.http.ktor.utils.githubApiVersion
import io.github.zenhelix.github.client.http.model.ErrorResponse
import io.github.zenhelix.github.client.http.model.HttpResponseResult
import io.github.zenhelix.github.client.http.model.LicensesResponse
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

public class GithubApiKtorClient(
    engine: HttpClientEngine,
    private val baseUrl: String = GITHUB_API_PUBLIC_BASE_URL,
    private val defaultToken: String? = null,
    configure: HttpClientConfig<*>.() -> Unit = {}
) : GithubCoroutineApi {

    private val client: HttpClient = HttpClient(engine) {
        install(DefaultRequest) {
            acceptGithubJson()
            githubApiVersion()
        }
        install(ContentNegotiation) {
            json(Json { prettyPrint = true })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }
        configure()
    }

    override suspend fun artifacts(owner: String, repository: String) {
        TODO()
    }

    override suspend fun licenses(token: String?): HttpResponseResult<LicensesResponse, ErrorResponse> = client
        .get("$baseUrl/licenses") { bearerAuth(requiredToken(token)) }.result()

    public fun close() {
        client.close()
    }

    private fun requiredToken(token: String?): String = token ?: defaultToken ?: throw IllegalArgumentException("Token is required")

}