package io.github.zenhelix.github.client.http.model.action

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an artifact returned by the GitHub Actions API.
 *
 * @property id The unique identifier of the artifact.
 * @property nodeId The node ID of the artifact, which is a GitHub-specific identifier.
 * @property name The name of the artifact as specified during upload.
 * @property sizeInBytes The total size of the artifact in bytes.
 * @property url The API URL to fetch details about this specific artifact.
 * @property archiveDownloadUrl The direct download URL for the artifact's archive.
 * @property expired Indicates whether the artifact has expired and is no longer available.
 * @property createdAt The timestamp when the artifact was created, in ISO 8601 format.
 * @property expiresAt The timestamp when the artifact will expire, in ISO 8601 format.
 * @property updatedAt The timestamp of the last update to the artifact, in ISO 8601 format.
 * @property digest The SHA256 digest of the artifact's content, if available (for newer upload-artifact versions).
 * @property workflowRun Optional information about the workflow run that created this artifact.
 *
 * @sample
 * ```json
 * {
 *   "id": 11,
 *   "node_id": "MDg6QXJ0aWZhY3QxMQ==",
 *   "name": "Rails",
 *   "size_in_bytes": 556,
 *   "url": "https://api.github.com/repos/octo-org/octo-docs/actions/artifacts/11",
 *   "archive_download_url": "https://api.github.com/repos/octo-org/octo-docs/actions/artifacts/11/zip",
 *   "expired": false,
 *   "created_at": "2020-01-10T14:59:22Z",
 *   "expires_at": "2020-03-21T14:59:22Z",
 *   "updated_at": "2020-02-21T14:59:22Z",
 *   "digest": "sha256:cfc3236bdad15b5898bca8408945c9e19e1917da8704adc20eaa618444290a8c",
 *   "workflow_run": {
 *     "id": 2332938,
 *     "repository_id": 1296269,
 *     "head_repository_id": 1296269,
 *     "head_branch": "main",
 *     "head_sha": "328faa0536e6fef19753d9d91dc96a9931694ce3"
 *   }
 * }
 * ```
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
 * Represents summarized information about a workflow run associated with an artifact.
 *
 * @property id The unique identifier of the workflow run.
 * @property repositoryId The GitHub repository ID where the workflow run occurred.
 * @property headRepositoryId The repository ID of the head repository.
 * @property headBranch The branch where the workflow run was triggered.
 * @property headSha The full Git SHA of the commit that triggered the workflow run.
 */
@Serializable
public data class WorkflowRunSummary(
    val id: Long,
    @SerialName("repository_id")
    val repositoryId: Long,
    @SerialName("head_repository_id")
    val headRepositoryId: Long,
    @SerialName("head_branch")
    val headBranch: String,
    @SerialName("head_sha")
    val headSha: String
)

/**
 * Represents the API response for retrieving a single artifact.
 *
 * @property artifact The detailed artifact information.
 */
@Serializable
public data class ArtifactResponse(
    val artifact: Artifact
)

/**
 * Represents the API response for listing artifacts in a repository.
 *
 * @property totalCount The total number of artifacts in the repository.
 * @property artifacts The list of artifacts.
 */
@Serializable
public data class ArtifactsResponse(
    @SerialName("total_count")
    val totalCount: Int,
    val artifacts: List<Artifact>
)

/**
 * Represents the API response for listing artifacts associated with a specific workflow run.
 *
 * @property totalCount The total number of artifacts for the workflow run.
 * @property artifacts The list of artifacts for the workflow run.
 */
@Serializable
public data class WorkflowRunArtifactsResponse(
    @SerialName("total_count")
    val totalCount: Int,
    val artifacts: List<Artifact>
)