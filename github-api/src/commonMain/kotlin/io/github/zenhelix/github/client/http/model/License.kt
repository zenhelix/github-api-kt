package io.github.zenhelix.github.client.http.model

import io.github.zenhelix.github.client.http.model.license.License

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