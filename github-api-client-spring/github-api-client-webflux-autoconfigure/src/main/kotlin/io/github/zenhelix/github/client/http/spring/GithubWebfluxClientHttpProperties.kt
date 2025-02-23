package io.github.zenhelix.github.client.http.spring

import io.github.zenhelix.github.client.http.spring.GithubWebfluxClientHttpProperties.Companion.GITHUB_WEBFLUX_HTTP_CLIENT_PROPERTIES_NAME
import io.github.zenhelix.spring.autoconfiguration.web.client.properties.reactor.netty.NettyReactorHttpClientProperties
import jakarta.validation.Valid
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.boot.context.properties.bind.Name
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(GITHUB_WEBFLUX_HTTP_CLIENT_PROPERTIES_NAME, ignoreUnknownFields = false)
public class GithubWebfluxClientHttpProperties {

    @NestedConfigurationProperty @field:Valid
    @Name("http")
    public val http: NettyReactorHttpClientProperties = NettyReactorHttpClientProperties()

    public val defaultToken: String? = null

    public companion object {
        public const val GITHUB_WEBFLUX_HTTP_CLIENT_PROPERTIES_NAME: String = "client.github.web"
    }

}
