package io.github.zenhelix.github.client.http.spring

import io.github.zenhelix.github.client.http.GithubApi
import io.github.zenhelix.github.client.http.GithubConstants.GITHUB_API_PUBLIC_BASE_URL
import io.github.zenhelix.github.client.http.rest.GithubApiRestClient
import io.github.zenhelix.spring.autoconfiguration.web.client.apache.CustomHttpComponentsClientHttpRequestFactoryBuilder
import io.github.zenhelix.spring.autoconfiguration.web.client.properties.apache.utils.ApacheHttpClientSpringAutoconfigureUtils.httpComponentsClientHttpRequestFactory
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings
import org.springframework.context.annotation.Bean
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestClient

@ConditionalOnClass(CloseableHttpClient::class)
@AutoConfiguration(after = [RestClientAutoConfiguration::class])
@EnableConfigurationProperties(GithubRestClientHttpProperties::class)
public class GithubClientRestAutoConfiguration(private val properties: GithubRestClientHttpProperties) {

    @Bean
    @ConditionalOnMissingBean(GithubApi::class)
    public fun githubApiClient(
        restClientBuilder: RestClient.Builder,
        @Qualifier("githubApiRestHttpComponentsRequestFactory")
        requestFactory: ClientHttpRequestFactory
    ): GithubApiRestClient = GithubApiRestClient(
        restClientBuilder = restClientBuilder.requestFactory(requestFactory),
        baseUrl = properties.http.baseUrl ?: GITHUB_API_PUBLIC_BASE_URL,
        defaultToken = properties.defaultToken
    )

    @Bean
    @ConditionalOnMissingBean(name = ["githubApiRestHttpComponentsRequestFactory"])
    public fun githubApiRestHttpComponentsRequestFactory(
        builder: CustomHttpComponentsClientHttpRequestFactoryBuilder,
        clientHttpRequestFactorySettings: ObjectProvider<ClientHttpRequestFactorySettings>
    ): HttpComponentsClientHttpRequestFactory = httpComponentsClientHttpRequestFactory(
        properties.http, builder, clientHttpRequestFactorySettings.ifAvailable
    )

}