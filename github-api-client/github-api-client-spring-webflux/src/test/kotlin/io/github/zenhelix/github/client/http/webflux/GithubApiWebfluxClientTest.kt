package io.github.zenhelix.github.client.http.webflux

import io.github.zenhelix.github.client.http.GithubConstants.APPLICATION_GITHUB_JSON_MEDIA_TYPE
import io.github.zenhelix.github.client.http.model.License
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import kotlin.test.assertEquals

class GithubApiWebfluxClientTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var client: GithubApiWebfluxClient

    private companion object {
        private const val MOCK_TOKEN = "mock-token"
    }

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        client = GithubApiWebfluxClient(baseUrl = mockWebServer.url("/").toString(), defaultToken = MOCK_TOKEN)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test licenses success`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(
                    //language=JSON
                    """
                    [
                        {"key":"agpl-3.0","name":"GNU Affero General Public License v3.0","spdx_id":"AGPL-3.0","url":"https://api.github.com/licenses/agpl-3.0","node_id":"MDc6TGljZW5zZTE="},
                        {"key":"apache-2.0","name":"Apache License 2.0","spdx_id":"Apache-2.0","url":"https://api.github.com/licenses/apache-2.0","node_id":"MDc6TGljZW5zZTI="}
                    ]
                    """.trimIndent()
                )
        )

        val result = client.licenses()

        mockWebServer.takeRequest().also {
            assertEquals("GET", it.method)
            assertEquals("/licenses", it.path)
            assertEquals("Bearer $MOCK_TOKEN", it.getHeader(HttpHeaders.AUTHORIZATION))
            assertEquals("${APPLICATION_GITHUB_JSON_MEDIA_TYPE}, ${MediaType.APPLICATION_JSON_VALUE}", it.getHeader(HttpHeaders.ACCEPT))
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
                )
            ),
            result
        )

    }

}