package io.github.zenhelix.github.client.http.ktor

import io.ktor.client.HttpClientConfig

public expect fun configureEngine(httpClientConfig: HttpClientConfig<*>)