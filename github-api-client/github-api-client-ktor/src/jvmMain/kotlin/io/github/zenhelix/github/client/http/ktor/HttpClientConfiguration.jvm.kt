package io.github.zenhelix.github.client.http.ktor

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIOEngineConfig

@Suppress("UNCHECKED_CAST")
public actual fun configureEngine(httpClientConfig: HttpClientConfig<*>) {
    (httpClientConfig as HttpClientConfig<CIOEngineConfig>).engine {
    }
}