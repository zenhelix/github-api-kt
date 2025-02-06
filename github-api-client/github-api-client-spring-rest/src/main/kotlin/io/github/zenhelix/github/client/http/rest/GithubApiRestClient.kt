package io.github.zenhelix.github.client.http.rest

import io.github.zenhelix.github.client.http.GithubApi
import io.github.zenhelix.github.client.http.GithubConstants.GITHUB_API_PUBLIC_BASE_URL
import io.github.zenhelix.github.client.http.model.LicensesResponse
import io.github.zenhelix.github.client.http.rest.utils.acceptGithubJson
import io.github.zenhelix.github.client.http.rest.utils.bearer
import io.github.zenhelix.github.client.http.rest.utils.githubVersion
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

public class GithubApiRestClient(
    restClientBuilder: RestClient.Builder = RestClient.builder(),
    baseUrl: String = GITHUB_API_PUBLIC_BASE_URL,
    private val defaultToken: String? = null
) : GithubApi {

    private val restClient = restClientBuilder
        .baseUrl(baseUrl)
        .defaultHeaders { it.acceptGithubJson().githubVersion() }
        .messageConverters { it.add(0, KotlinSerializationJsonHttpMessageConverter()) }
        .build()

    override fun artifacts(owner: String, repository: String) {
        TODO("Not yet implemented")
    }

    override fun licenses(token: String?): LicensesResponse = restClient
        .get().uri("/licenses")
        .bearer(requiredToken(token))
        .retrieve().body()!!

    private fun requiredToken(token: String?): String = defaultToken ?: token ?: throw IllegalArgumentException("Token is required")

}
