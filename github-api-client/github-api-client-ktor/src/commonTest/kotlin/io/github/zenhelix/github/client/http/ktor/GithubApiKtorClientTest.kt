package io.github.zenhelix.github.client.http.ktor

import io.github.zenhelix.github.client.http.GithubApiVersion
import io.github.zenhelix.github.client.http.GithubConstants.APPLICATION_GITHUB_JSON_MEDIA_TYPE
import io.github.zenhelix.github.client.http.GithubConstants.GITHUB_API_PUBLIC_BASE_URL
import io.github.zenhelix.github.client.http.ktor.utils.GithubApiVersion
import io.github.zenhelix.github.client.http.model.License
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class GithubApiKtorClientTest {

    @Test fun `success request`() {
        val mockBearer = "mock"

        val mockEngine = MockEngine { request ->
            assertEquals("$GITHUB_API_PUBLIC_BASE_URL/licenses", request.url.toString())
            assertEquals("Bearer $mockBearer", request.headers[HttpHeaders.Authorization])
            assertEquals(APPLICATION_GITHUB_JSON_MEDIA_TYPE, request.headers[HttpHeaders.Accept])
            assertEquals(GithubApiVersion.V_2022_11_28.version, request.headers[HttpHeaders.GithubApiVersion])

            respond(
                content = ByteReadChannel(
                    //language=JSON
                    """[{"key":"agpl-3.0","name":"GNU Affero General Public License v3.0","spdx_id":"AGPL-3.0","url":"https://api.github.com/licenses/agpl-3.0","node_id":"MDc6TGljZW5zZTE="},{"key":"apache-2.0","name":"Apache License 2.0","spdx_id":"Apache-2.0","url":"https://api.github.com/licenses/apache-2.0","node_id":"MDc6TGljZW5zZTI="},{"key":"bsd-2-clause","name":"BSD 2-Clause \"Simplified\" License","spdx_id":"BSD-2-Clause","url":"https://api.github.com/licenses/bsd-2-clause","node_id":"MDc6TGljZW5zZTQ="},{"key":"bsd-3-clause","name":"BSD 3-Clause \"New\" or \"Revised\" License","spdx_id":"BSD-3-Clause","url":"https://api.github.com/licenses/bsd-3-clause","node_id":"MDc6TGljZW5zZTU="},{"key":"bsl-1.0","name":"Boost Software License 1.0","spdx_id":"BSL-1.0","url":"https://api.github.com/licenses/bsl-1.0","node_id":"MDc6TGljZW5zZTI4"},{"key":"cc0-1.0","name":"Creative Commons Zero v1.0 Universal","spdx_id":"CC0-1.0","url":"https://api.github.com/licenses/cc0-1.0","node_id":"MDc6TGljZW5zZTY="},{"key":"epl-2.0","name":"Eclipse Public License 2.0","spdx_id":"EPL-2.0","url":"https://api.github.com/licenses/epl-2.0","node_id":"MDc6TGljZW5zZTMy"},{"key":"gpl-2.0","name":"GNU General Public License v2.0","spdx_id":"GPL-2.0","url":"https://api.github.com/licenses/gpl-2.0","node_id":"MDc6TGljZW5zZTg="},{"key":"gpl-3.0","name":"GNU General Public License v3.0","spdx_id":"GPL-3.0","url":"https://api.github.com/licenses/gpl-3.0","node_id":"MDc6TGljZW5zZTk="},{"key":"lgpl-2.1","name":"GNU Lesser General Public License v2.1","spdx_id":"LGPL-2.1","url":"https://api.github.com/licenses/lgpl-2.1","node_id":"MDc6TGljZW5zZTEx"},{"key":"mit","name":"MIT License","spdx_id":"MIT","url":"https://api.github.com/licenses/mit","node_id":"MDc6TGljZW5zZTEz"},{"key":"mpl-2.0","name":"Mozilla Public License 2.0","spdx_id":"MPL-2.0","url":"https://api.github.com/licenses/mpl-2.0","node_id":"MDc6TGljZW5zZTE0"},{"key":"unlicense","name":"The Unlicense","spdx_id":"Unlicense","url":"https://api.github.com/licenses/unlicense","node_id":"MDc6TGljZW5zZTE1"}]"""
                ),
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.AcceptRanges to listOf("bytes"),
                    HttpHeaders.AccessControlAllowOrigin to listOf("*"),
                    HttpHeaders.AccessControlExposeHeaders to listOf(
                        "ETag, Link, Location, Retry-After, X-GitHub-OTP, X-RateLimit-Limit, X-RateLimit-Remaining, X-RateLimit-Used, X-RateLimit-Resource, X-RateLimit-Reset, X-OAuth-Scopes, X-Accepted-OAuth-Scopes, X-Poll-Interval, X-GitHub-Media-Type, X-GitHub-SSO, X-GitHub-Request-Id, Deprecation, Sunset"
                    ),
                    HttpHeaders.CacheControl to listOf("public", "max-age=60", "s-maxage=60"),
                    HttpHeaders.ContentLength to listOf("2037"),
                    "Content-Security-Policy:" to listOf("default-src 'none'"),
                    HttpHeaders.ContentType to listOf("application/json"),
                    HttpHeaders.Date to listOf("Wed, 05 Feb 2025 15:52:34 GMT"),
                    HttpHeaders.ETag to listOf("W/\"f0483b0fa41fbbfed05fe7cca58b2bf52789eb6cf225b5be7b921e70554d48f4\""),
                    "Referrer-Policy" to listOf("origin-when-cross-origin", "strict-origin-when-cross-origin"),
                    HttpHeaders.Server to listOf("github.com"),
                    HttpHeaders.StrictTransportSecurity to listOf("max-age=31536000; includeSubdomains; preload"),
                    HttpHeaders.Vary to listOf("Accept", "Accept-Encoding", "X-Requested-With"),
                    "X-Content-Type-Options" to listOf("nosniff"),
                    "X-Frame-Options" to listOf("deny"),
                    "X-GitHub-Media-Type" to listOf("github.v3"),
                    "X-GitHub-Request-Id" to listOf("577E:9FCA3:11300B:17B6A3:67A38942"),
                    "X-RateLimit-Limit" to listOf("60"),
                    "X-RateLimit-Remaining" to listOf("33"),
                    "X-RateLimit-Reset" to listOf("1738772393"),
                    "X-RateLimit-Resource" to listOf("core"),
                    "X-RateLimit-Used" to listOf("27"),
                    "X-XSS-Protection" to listOf("0"),
                    "x-github-api-version-selected" to listOf("2022-11-28")
                )
            )
        }

        assertEquals(
            listOf(
                License(
                    key = "agpl-3.0",
                    name = "GNU Affero General Public License v3.0",
                    spdxId = "AGPL-3.0",
                    url = "https://api.github.com/licenses/agpl-3.0",
                    nodeId = "MDc6TGljZW5zZTE="
                ),
                License(
                    key = "apache-2.0",
                    name = "Apache License 2.0",
                    spdxId = "Apache-2.0",
                    url = "https://api.github.com/licenses/apache-2.0",
                    nodeId = "MDc6TGljZW5zZTI="
                ),
                License(
                    key = "bsd-2-clause",
                    name = "BSD 2-Clause \"Simplified\" License",
                    spdxId = "BSD-2-Clause",
                    url = "https://api.github.com/licenses/bsd-2-clause",
                    nodeId = "MDc6TGljZW5zZTQ="
                ),
                License(
                    key = "bsd-3-clause",
                    name = "BSD 3-Clause \"New\" or \"Revised\" License",
                    spdxId = "BSD-3-Clause",
                    url = "https://api.github.com/licenses/bsd-3-clause",
                    nodeId = "MDc6TGljZW5zZTU="
                ),
                License(
                    key = "bsl-1.0",
                    name = "Boost Software License 1.0",
                    spdxId = "BSL-1.0",
                    url = "https://api.github.com/licenses/bsl-1.0",
                    nodeId = "MDc6TGljZW5zZTI4"
                ),
                License(
                    key = "cc0-1.0",
                    name = "Creative Commons Zero v1.0 Universal",
                    spdxId = "CC0-1.0",
                    url = "https://api.github.com/licenses/cc0-1.0",
                    nodeId = "MDc6TGljZW5zZTY="
                ),
                License(
                    key = "epl-2.0",
                    name = "Eclipse Public License 2.0",
                    spdxId = "EPL-2.0",
                    url = "https://api.github.com/licenses/epl-2.0",
                    nodeId = "MDc6TGljZW5zZTMy"
                ),
                License(
                    key = "gpl-2.0",
                    name = "GNU General Public License v2.0",
                    spdxId = "GPL-2.0",
                    url = "https://api.github.com/licenses/gpl-2.0",
                    nodeId = "MDc6TGljZW5zZTg="
                ),
                License(
                    key = "gpl-3.0",
                    name = "GNU General Public License v3.0",
                    spdxId = "GPL-3.0",
                    url = "https://api.github.com/licenses/gpl-3.0",
                    nodeId = "MDc6TGljZW5zZTk="
                ),
                License(
                    key = "lgpl-2.1",
                    name = "GNU Lesser General Public License v2.1",
                    spdxId = "LGPL-2.1",
                    url = "https://api.github.com/licenses/lgpl-2.1",
                    nodeId = "MDc6TGljZW5zZTEx"
                ),
                License(key = "mit", name = "MIT License", spdxId = "MIT", url = "https://api.github.com/licenses/mit", nodeId = "MDc6TGljZW5zZTEz"),
                License(
                    key = "mpl-2.0",
                    name = "Mozilla Public License 2.0",
                    spdxId = "MPL-2.0",
                    url = "https://api.github.com/licenses/mpl-2.0",
                    nodeId = "MDc6TGljZW5zZTE0"
                ),
                License(
                    key = "unlicense",
                    name = "The Unlicense",
                    spdxId = "Unlicense",
                    url = "https://api.github.com/licenses/unlicense",
                    nodeId = "MDc6TGljZW5zZTE1"
                )
            ),
            runBlocking { GithubApiKtorClient(mockEngine, defaultToken = mockBearer).licenses() }
        )
    }

}