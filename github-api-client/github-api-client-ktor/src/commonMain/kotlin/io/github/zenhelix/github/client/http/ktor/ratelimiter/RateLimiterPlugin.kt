package io.github.zenhelix.github.client.http.ktor.ratelimiter

import io.ktor.client.HttpClient
import io.ktor.client.plugins.api.ClientPlugin
import io.ktor.client.plugins.api.ClientPluginBuilder
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.statement.request
import io.ktor.util.AttributeKey
import io.ktor.util.collections.ConcurrentMap

internal val RATE_LIMITER_NAME_GLOBAL = RateLimiterName("KTOR_GLOBAL_RATE_LIMITER")

internal val RateLimiterInstancesRegistryKey = AttributeKey<ConcurrentMap<RateLimiterName, RateLimiter>>("RateLimiterInstancesRegistryKey")
internal val RateLimiterNameKey = AttributeKey<RateLimiterName>("RateLimiterInstancesRegistryKey")

public val RateLimiting: ClientPlugin<RateLimiterConfig> = createClientPlugin("RateLimiter", ::RateLimiterConfig) {
    val instances = pluginConfig.rateLimiters.apply {
        pluginConfig.global?.also { put(RATE_LIMITER_NAME_GLOBAL, it) }
    }.also { require(it.isNotEmpty()) { "At least one rate limiter must be specified" } }

    client.rateLimiterRegistry().putAll(instances.mapValues {
        it.value.apply { initialize(client.engine.dispatcher) }
    })

    rateLimiterPluginBuilder()
}

internal fun HttpClient.rateLimiterRegistry() = this.attributes.computeIfAbsent(RateLimiterInstancesRegistryKey) { ConcurrentMap() }

internal fun ClientPluginBuilder<RateLimiterConfig>.rateLimiterPluginBuilder() {
    val instanceRegistry = client.rateLimiterRegistry()

    onRequest { request, _ ->
        request.attributes.getOrNull(RateLimiterNameKey)?.let { rateLimiterName ->
            instanceRegistry.getValue(rateLimiterName).waitIfNeeded()
        }
    }

    onResponse { response ->
        response.request.attributes.getOrNull(RateLimiterNameKey)?.let { rateLimiterName ->
            instanceRegistry.getValue(rateLimiterName).handleResponse(response)
        }
    }

}