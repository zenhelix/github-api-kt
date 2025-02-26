package io.github.zenhelix.github.client.http.model.license

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a list of licenses returned by the GitHub API.
 *
 * @sample
 * ```json
 * [
 *   {
 *     "key": "mit",
 *     "name": "MIT License",
 *     "spdx_id": "MIT",
 *     "url": "https://api.github.com/licenses/mit",
 *     "node_id": "MDc6TGljZW5zZTEz"
 *   },
 *   {
 *     "key": "apache-2.0",
 *     "name": "Apache License 2.0",
 *     "spdx_id": "Apache-2.0",
 *     "url": "https://api.github.com/licenses/apache-2.0",
 *     "node_id": "MDc6TGljZW5zZTI="
 *   }
 * ]
 * ```
 */
public typealias LicensesResponse = List<License>

/**
 * Represents a license object returned by the GitHub API.
 *
 * @property key Unique identifier for the license (e.g., "mit").
 * @property name Human-readable name of the license (e.g., "MIT License").
 * @property spdxId SPDX identifier for the license, if available (e.g., "MIT").
 * @property url URL to the license details on GitHub, if available.
 * @property nodeId Internal GitHub node ID for the license (e.g., "MDc6TGljZW5zZTEz").
 *
 * @sample
 * ```json
 * {
 *   "key": "mit",
 *   "name": "MIT License",
 *   "spdx_id": "MIT",
 *   "url": "https://api.github.com/licenses/mit",
 *   "node_id": "MDc6TGljZW5zZTEz"
 * }
 * ```
 */
@Serializable
public data class License(
    /**
     * A unique identifier for the license.
     *
     * Example: `"mit"` for the MIT License.
     */
    val key: String,

    /**
     * The human-readable name of the license.
     *
     * Example: `"MIT License"`.
     */
    val name: String,

    /**
     * The SPDX identifier for the license, if available.
     *
     * SPDX is a standard format for identifying licenses.
     * Example: `"MIT"` for the MIT License.
     *
     * This field may be `null` if the license does not have an SPDX identifier.
     */
    @SerialName("spdx_id")
    val spdxId: String?,

    /**
     * The URL to the license details on GitHub.
     *
     * This field may be `null` if the URL is not available.
     *
     * Example: `"https://api.github.com/licenses/mit"`.
     */
    val url: String?,

    /**
     * The internal GitHub node ID for the license.
     *
     * This is a unique identifier used internally by GitHub.
     *
     * Example: `"MDc6TGljZW5zZTEz"`.
     */
    @SerialName("node_id")
    val nodeId: String
)