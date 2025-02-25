package io.github.zenhelix.github.client.http.spring

import io.github.zenhelix.github.client.http.GithubConstants.GITHUB_API_PUBLIC_BASE_URL
import io.github.zenhelix.github.client.http.GithubCoroutineApi
import io.github.zenhelix.github.client.http.webflux.GithubApiWebfluxClient
import io.github.zenhelix.spring.autoconfiguration.web.client.properties.reactor.netty.utils.NettyReactorHttpClientSpringAutoconfigureUtils.reactorClientHttpConnector
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.http.client.ReactorResourceFactory
import org.springframework.http.client.reactive.ClientHttpConnector
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@ConditionalOnClass(HttpClient::class)
@AutoConfiguration(after = [WebClientAutoConfiguration::class])
@EnableConfigurationProperties(GithubWebfluxClientHttpProperties::class)
public class GithubClientWebfluxAutoConfiguration(private val properties: GithubWebfluxClientHttpProperties) {

    @Bean
    @ConditionalOnMissingBean(GithubCoroutineApi::class)
    public fun githubApiClient(
        webClientBuilder: WebClient.Builder,
        @Qualifier("githubApiReactorClientHttpConnector")
        clientHttpConnector: ClientHttpConnector
    ): GithubApiWebfluxClient = GithubApiWebfluxClient(
        webClientBuilder = webClientBuilder.clientConnector(clientHttpConnector),
        baseUrl = properties.http.baseUrl ?: GITHUB_API_PUBLIC_BASE_URL,
        defaultToken = properties.defaultToken
    )

    @Bean
    @ConditionalOnMissingBean(name = ["githubApiReactorClientHttpConnector"])
    public fun githubApiReactorClientHttpConnector(
        resourceFactory: ReactorResourceFactory
    ): ReactorClientHttpConnector = reactorClientHttpConnector(properties.http, resourceFactory)

}