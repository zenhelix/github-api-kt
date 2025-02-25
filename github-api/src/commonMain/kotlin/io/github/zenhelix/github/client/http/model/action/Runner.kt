package io.github.zenhelix.github.client.http.model.action

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a self-hosted runner for GitHub Actions.
 *
 * @property id The ID of the runner.
 * @property name The name of the runner.
 * @property os The operating system of the runner.
 * @property status The status of the runner.
 * @property busy Whether the runner is currently busy.
 * @property labels The labels assigned to the runner.
 * @property runnerGroupId The ID of the runner group.
 * @property runnerGroupName The name of the runner group.
 */
@Serializable
public data class Runner(
    val id: Long,
    val name: String,
    val os: String,
    val status: String,
    val busy: Boolean,
    val labels: List<RunnerLabel>,
    @SerialName("runner_group_id")
    val runnerGroupId: Long,
    @SerialName("runner_group_name")
    val runnerGroupName: String
)

/**
 * Represents a label assigned to a runner.
 *
 * @property id The ID of the label.
 * @property name The name of the label.
 * @property type The type of label (e.g., "system" or "custom").
 */
@Serializable
public data class RunnerLabel(
    val id: Long,
    val name: String,
    val type: String
)

/**
 * Response for listing self-hosted runners.
 *
 * @property totalCount The total number of runners.
 * @property runners The list of runners.
 */
@Serializable
public data class RunnersResponse(
    @SerialName("total_count")
    val totalCount: Int,
    val runners: List<Runner>
)

/**
 * Represents a registration token for a self-hosted runner.
 *
 * @property token The token value.
 * @property expiresAt The expiration date of the token.
 */
@Serializable
public data class RunnerRegistrationToken(
    val token: String,
    @SerialName("expires_at")
    val expiresAt: String
)

/**
 * Represents a remove token for a self-hosted runner.
 *
 * @property token The token value.
 * @property expiresAt The expiration date of the token.
 */
@Serializable
public data class RunnerRemoveToken(
    val token: String,
    @SerialName("expires_at")
    val expiresAt: String
)

/**
 * Represents the runner application download information.
 *
 * @property os The operating system of the runner application.
 * @property architecture The architecture of the runner application.
 * @property downloadUrl The URL to download the runner application.
 * @property filename The filename of the runner application.
 * @property tempDownloadToken A temporary download token.
 * @property sha256Checksum The SHA-256 checksum of the runner application.
 */
@Serializable
public data class RunnerApplication(
    val os: String,
    val architecture: String,
    @SerialName("download_url")
    val downloadUrl: String,
    val filename: String,
    @SerialName("temp_download_token")
    val tempDownloadToken: String? = null,
    @SerialName("sha256_checksum")
    val sha256Checksum: String? = null
)

/**
 * Response for listing runner applications.
 *
 * @property totalCount The total number of runner applications.
 * @property runnerApplications The list of runner applications.
 */
@Serializable
public data class RunnerApplicationsResponse(
    @SerialName("total_count")
    val totalCount: Int,
    @SerialName("runner_applications")
    val runnerApplications: List<RunnerApplication>
)