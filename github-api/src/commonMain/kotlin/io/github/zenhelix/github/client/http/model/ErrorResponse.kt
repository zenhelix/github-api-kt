package io.github.zenhelix.github.client.http.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ErrorResponse(
    val message: String?,
    @SerialName("documentation_url")
    val documentationUrl: String?,
    val status: String?
)
