package io.github.zenhelix.github.client.http.model.action

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a GitHub Actions cache entry.
 *
 * @property id Unique identifier of the cache.
 * @property ref The Git reference for the cache.
 * @property key A key identifying the cache.
 * @property version Version of the cache.
 * @property lastAccessedAt Timestamp of the last cache access.
 * @property createdAt Timestamp when the cache was created.
 * @property sizeInBytes Size of the cache in bytes.
 */
@Serializable
public data class Cache(
    val id: Long,
    val ref: String?,
    val key: String,
    val version: String,
    @SerialName("last_accessed_at")
    val lastAccessedAt: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("size_in_bytes")
    val sizeInBytes: Long
)

/**
 * Response for listing caches of a repository.
 *
 * @property totalCount Total number of caches.
 * @property caches List of caches in the repository.
 */
@Serializable
public data class CacheListResponse(
    @SerialName("total_count")
    val totalCount: Int,
    @SerialName("actions_caches")
    val caches: List<Cache>
)

/**
 * Response for a cache usage request for a repository.
 *
 * @property fullName Full name of the repository (owner/repo).
 * @property activeCachesCount Number of active caches.
 * @property activeCachesSizeInBytes Total size of active caches in bytes.
 */
@Serializable
public data class CacheUsageResponse(
    @SerialName("full_name")
    val fullName: String,
    @SerialName("active_caches_count")
    val activeCachesCount: Int,
    @SerialName("active_caches_size_in_bytes")
    val activeCachesSizeInBytes: Long
)

/**
 * Response for deleting caches by key.
 *
 * @property totalCount Number of deleted caches.
 * @property caches List of deleted caches.
 */
@Serializable
public data class DeleteCachesByKeyResponse(
    @SerialName("total_count")
    val totalCount: Int,
    @SerialName("actions_caches")
    val caches: List<Cache>
)

/**
 * Response for GitHub Actions cache usage for an organization.
 *
 * @property totalActiveCachesSizeInBytes Total size of active caches in bytes.
 * @property totalActiveCachesCount Total number of active caches.
 */
@Serializable
public data class OrganizationCacheUsageResponse(
    @SerialName("total_active_caches_size_in_bytes")
    val totalActiveCachesSizeInBytes: Long,
    @SerialName("total_active_caches_count")
    val totalActiveCachesCount: Int
)

/**
 * Represents repository cache usage information.
 *
 * @property fullName Full name of the repository (owner/repo).
 * @property activeCachesSizeInBytes Total size of active caches for the repository.
 * @property activeCachesCount Number of active caches for the repository.
 */
@Serializable
public data class RepositoryCacheUsage(
    @SerialName("full_name")
    val fullName: String,
    @SerialName("active_caches_size_in_bytes")
    val activeCachesSizeInBytes: Long,
    @SerialName("active_caches_count")
    val activeCachesCount: Int
)

/**
 * Response for listing repositories with GitHub Actions cache usage for an organization.
 *
 * @property totalCount Total number of repositories.
 * @property repositoryCacheUsages List of repositories with cache usage information.
 */
@Serializable
public data class OrganizationRepositoriesCacheUsageResponse(
    @SerialName("total_count")
    val totalCount: Int,
    @SerialName("repository_cache_usages")
    val repositoryCacheUsages: List<RepositoryCacheUsage>
)