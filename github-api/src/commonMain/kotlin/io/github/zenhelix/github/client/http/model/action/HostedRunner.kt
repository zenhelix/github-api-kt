package io.github.zenhelix.github.client.http.model.action

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a GitHub-hosted runner for an organization.
 *
 * @property id Unique identifier of the hosted runner.
 * @property name Name of the hosted runner.
 * @property runnerGroupId Unique identifier of the runner group.
 * @property platform Operating system of the runner (e.g., "linux-x64", "win-x64").
 * @property image Details of the runner's image.
 * @property machineSize Details of the machine specifications.
 * @property status Current status of the runner.
 * @property maximumRunners Maximum number of runners that can be scaled.
 * @property publicIpEnabled Whether public IP is enabled for the runner.
 * @property publicIps List of public IP ranges when enabled.
 * @property lastActiveOn Timestamp of the last runner activity.
 */
@Serializable
public data class HostedRunner(
    val id: Long,
    val name: String,
    @SerialName("runner_group_id")
    val runnerGroupId: Long,
    val platform: String,
    @SerialName("image")
    val image: RunnerImage?,
    @SerialName("machine_size_details")
    val machineSize: MachineSizeDetails,
    val status: RunnerStatus,
    @SerialName("maximum_runners")
    val maximumRunners: Int,
    @SerialName("public_ip_enabled")
    val publicIpEnabled: Boolean,
    @SerialName("public_ips")
    val publicIps: List<PublicIpDetails>,
    @SerialName("last_active_on")
    val lastActiveOn: String?
)

/**
 * Represents the image details for a GitHub-hosted runner.
 *
 * @property id Unique identifier of the image.
 * @property sizeGb Image size in gigabytes.
 * @property displayName Display name of the image.
 * @property source Provider of the image (github, partner, custom).
 * @property version Version of the image.
 */
@Serializable
public data class RunnerImage(
    val id: String,
    @SerialName("size_gb")
    val sizeGb: Int,
    @SerialName("display_name")
    val displayName: String,
    val source: String,
    val version: String? = null
)

/**
 * Represents the machine specifications for a GitHub-hosted runner.
 *
 * @property id Identifier for the machine size.
 * @property cpuCores Number of CPU cores.
 * @property memoryGb Available RAM in gigabytes.
 * @property storageGb Available SSD storage in gigabytes.
 */
@Serializable
public data class MachineSizeDetails(
    val id: String,
    @SerialName("cpu_cores")
    val cpuCores: Int,
    @SerialName("memory_gb")
    val memoryGb: Int,
    @SerialName("storage_gb")
    val storageGb: Int
)

/**
 * Represents public IP details for a GitHub-hosted runner.
 *
 * @property enabled Whether public IP is enabled.
 * @property prefix The prefix for the public IP.
 * @property length The length of the IP prefix.
 */
@Serializable
public data class PublicIpDetails(
    val enabled: Boolean,
    val prefix: String,
    val length: Int
)

/**
 * Enum representing possible statuses of a GitHub-hosted runner.
 */
@Serializable
public enum class RunnerStatus {
    Ready, Provisioning, Shutdown, Deleting, Stuck
}

/**
 * Response for listing GitHub-hosted runners.
 *
 * @property totalCount Total number of runners.
 * @property runners List of hosted runners.
 */
@Serializable
public data class HostedRunnersResponse(
    @SerialName("total_count")
    val totalCount: Int,
    val runners: List<HostedRunner>
)

/**
 * Response for runner image details.
 *
 * @property totalCount Total number of images.
 * @property images List of runner images.
 */
@Serializable
public data class RunnerImagesResponse(
    @SerialName("total_count")
    val totalCount: Int,
    val images: List<RunnerImage>
)

/**
 * Response for runner platform details.
 *
 * @property totalCount Total number of platforms.
 * @property platforms List of available platforms.
 */
@Serializable
public data class RunnerPlatformsResponse(
    @SerialName("total_count")
    val totalCount: Int,
    val platforms: List<String>
)

/**
 * Response for runner machine specs.
 *
 * @property totalCount Total number of machine specs.
 * @property machineSpecs List of machine specifications.
 */
@Serializable
public data class RunnerMachineSpecsResponse(
    @SerialName("total_count")
    val totalCount: Int,
    @SerialName("machine_specs")
    val machineSpecs: List<MachineSizeDetails>
)

/**
 * Response for runner limits.
 *
 * @property publicIps Details of public IP limits.
 */
@Serializable
public data class RunnerLimitsResponse(
    @SerialName("public_ips")
    val publicIps: PublicIpLimits
)

/**
 * Represents limits for public IP addresses.
 *
 * @property maximum Maximum number of static public IP addresses.
 * @property currentUsage Current number of static public IP addresses in use.
 */
@Serializable
public data class PublicIpLimits(
    val maximum: Int,
    @SerialName("current_usage")
    val currentUsage: Int
)