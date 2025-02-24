package io.github.zenhelix.github.client.http.ktor.circuitbreaker

import io.ktor.client.HttpClient
import io.ktor.client.plugins.api.ClientPlugin
import io.ktor.client.plugins.api.ClientPluginBuilder
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.statement.request
import io.ktor.util.AttributeKey
import io.ktor.util.collections.ConcurrentMap

internal val CIRCUIT_BREAKER_NAME_GLOBAL = CircuitBreakerName("KTOR_GLOBAL_RATE_LIMITER")

internal val CircuitBreakerInstancesRegistryKey = AttributeKey<ConcurrentMap<CircuitBreakerName, CircuitBreaker>>("CircuitBreakerInstancesRegistryKey")
internal val CircuitBreakerNameKey = AttributeKey<CircuitBreakerName>("CircuitBreakerInstancesRegistryKey")

public val CircuitBreaking: ClientPlugin<CircuitBreakerConfig> = createClientPlugin("CircuitBreaker", ::CircuitBreakerConfig) {
    val instances = pluginConfig.circuitBreakers.apply {
        pluginConfig.global?.also {
            put(CIRCUIT_BREAKER_NAME_GLOBAL, it)
        }
    }
    require(instances.isNotEmpty()) { "At least one circuit breaker must be specified" }
    client.circuitBreakerRegistry().putAll(instances.mapValues { entry ->
        entry.value.apply {
            initialize(client.engine.dispatcher)
        }
    })
    circuitBreakerPluginBuilder()
}

internal fun HttpClient.circuitBreakerRegistry() = this.attributes.computeIfAbsent(CircuitBreakerInstancesRegistryKey) { ConcurrentMap() }

internal fun ClientPluginBuilder<CircuitBreakerConfig>.circuitBreakerPluginBuilder() {
    val instanceRegistry = client.circuitBreakerRegistry()

    onRequest { request, _ ->
        request.attributes.getOrNull(CircuitBreakerNameKey)?.let { circuitBreakerName ->
            instanceRegistry.getValue(circuitBreakerName).wire()
        }
    }

    onResponse { response ->
        response.request.attributes.getOrNull(CircuitBreakerNameKey)?.let { circuitBreakerName ->
            instanceRegistry.getValue(circuitBreakerName).handleResponse(response)
        }
    }
}
