package io.github.zenhelix.github.client.http.model.action

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an artifact returned by the GitHub API.
 *
 * @property id The ID of the artifact.
 * @property nodeId The node ID of the artifact.
 * @property name The name of the artifact.
 * @property sizeInBytes The size of the artifact in bytes.
 * @property url The URL to download the artifact.
 * @property archiveDownloadUrl The URL to download the archive of the artifact.
 * @property expired Whether the artifact has expired.
 * @property createdAt The creation date of the artifact.
 * @property expiresAt The expiration date of the artifact.
 * @property updatedAt The last update date of the artifact.
 * @property digest The SHA256 digest of the artifact content.
 * @property workflowRun Optional information about the workflow run that created the artifact.
 */
@Serializable
public data class Artifact(
    val id: Long,
    @SerialName("node_id")
    val nodeId: String,
    val name: String,
    @SerialName("size_in_bytes")
    val sizeInBytes: Long,
    val url: String,
    @SerialName("archive_download_url")
    val archiveDownloadUrl: String,
    val expired: Boolean,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("expires_at")
    val expiresAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    val digest: String?,
    @SerialName("workflow_run")
    val workflowRun: WorkflowRunSummary?
)

/**
 * Summarized information about a workflow run.
 *
 * @property id The ID of the workflow run.
 * @property repositoryId The ID of the repository.
 * @property headSha The SHA of the head commit.
 * @property headBranch The branch of the head commit.
 */
@Serializable
public data class WorkflowRunSummary(
    val id: Long,
    @SerialName("repository_id")
    val repositoryId: Long,
    @SerialName("head_sha")
    val headSha: String,
    @SerialName("head_branch")
    val headBranch: String
)

/**
 * Response for a single artifact.
 */
@Serializable
public data class ArtifactResponse(
    val artifact: Artifact
)

/**
 * Response for listing artifacts of a repository.
 *
 * @property totalCount The total number of artifacts.
 * @property artifacts The list of artifacts.
 */
@Serializable
public data class ArtifactsResponse(
    @SerialName("total_count")
    val totalCount: Int,
    val artifacts: List<Artifact>
)

/**
 * Response for listing artifacts of a workflow run.
 *
 * @property totalCount The total number of artifacts.
 * @property artifacts The list of artifacts.
 */
@Serializable
public data class WorkflowRunArtifactsResponse(
    @SerialName("total_count")
    val totalCount: Int,
    val artifacts: List<Artifact>
)