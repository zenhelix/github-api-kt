package io.github.zenhelix.github.client.http.ktor

import io.github.zenhelix.github.client.http.GithubApiVersion
import io.github.zenhelix.github.client.http.GithubConstants.APPLICATION_GITHUB_JSON_MEDIA_TYPE
import io.github.zenhelix.github.client.http.GithubConstants.GITHUB_API_PUBLIC_BASE_URL
import io.github.zenhelix.github.client.http.GithubCoroutineApi
import io.github.zenhelix.github.client.http.ktor.utils.GithubApiVersion
import io.github.zenhelix.github.client.http.model.LicensesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessageBuilder
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

public class GithubApiKtorClient(
    engine: HttpClientEngine,
    private val baseUrl: String = GITHUB_API_PUBLIC_BASE_URL,
    private val defaultToken: String? = null
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
        configureEngine(this)
    }

    override suspend fun artifacts(owner: String, repository: String) {
        TODO()
    }

    override suspend fun licenses(token: String?): LicensesResponse = client
        .get("$baseUrl/licenses") { authorizationBearer(token) }.body()

    public fun close() {
        client.close()
    }

    private fun HttpMessageBuilder.authorizationBearer(token: String?): Unit =
        header(HttpHeaders.Authorization, "Bearer ${requiredToken(token)}")

    private fun HttpMessageBuilder.githubApiVersion(version: GithubApiVersion = GithubApiVersion.V_2022_11_28): Unit =
        header(HttpHeaders.GithubApiVersion, version.version)

    private fun HttpMessageBuilder.acceptGithubJson(): Unit =
        header(HttpHeaders.Accept, APPLICATION_GITHUB_JSON_MEDIA_TYPE)

    private fun requiredToken(token: String?): String {
        return defaultToken ?: token ?: throw IllegalArgumentException("Token is required")
    }

}