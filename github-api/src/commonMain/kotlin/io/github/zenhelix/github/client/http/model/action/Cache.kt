package io.github.zenhelix.github.client.http.model.action

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a GitHub Actions cache entry.
 *
 * @property id The ID of the cache.
 * @property ref The ref of the cache.
 * @property key The key used to identify the cache.
 * @property version The version of the cache.
 * @property lastAccessedAt The timestamp when the cache was last accessed.
 * @property createdAt The timestamp when the cache was created.
 * @property sizeInBytes The size of the cache in bytes.
 */
@Serializable
public data class Cache(
    val id: Long,
    val ref: String,
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
 * @property totalCount The total number of caches.
 * @property caches The list of caches.
 */
@Serializable
public data class CacheListResponse(
    @SerialName("total_count")
    val totalCount: Int,
    @SerialName("actions_caches")
    val caches: List<Cache>
)

/**
 * Response for a cache usage request.
 *
 * @property fullName The full name of the repository (owner/repo).
 * @property activeCachesCount The number of active caches.
 * @property activeCachesSizeInBytes The total size of active caches in bytes.
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
 * Response for a delete cache by key request.
 *
 * @property totalCount The total number of deleted caches.
 * @property caches The list of deleted caches.
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
 * @property totalActiveCachesSizeInBytes The total size of active caches in bytes.
 * @property totalActiveCachesCount The number of active caches.
 */
@Serializable
public data class OrganizationCacheUsageResponse(
    @SerialName("total_active_caches_size_in_bytes")
    val totalActiveCachesSizeInBytes: Long,
    @SerialName("total_active_caches_count")
    val totalActiveCachesCount: Int
)

/**
 * Repository cache usage information.
 *
 * @property fullName The full name of the repository (owner/repo).
 * @property activeCachesSizeInBytes The total size of active caches in bytes for this repository.
 * @property activeCachesCount The number of active caches for this repository.
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
 * @property totalCount The total number of repositories.
 * @property repositoryCacheUsages The list of repositories with cache usage information.
 */
@Serializable
public data class OrganizationRepositoriesCacheUsageResponse(
    @SerialName("total_count")
    val totalCount: Int,
    @SerialName("repository_cache_usages")
    val repositoryCacheUsages: List<RepositoryCacheUsage>
)