package io.github.zenhelix.github.client.http.rest

import io.github.zenhelix.github.client.http.GithubApiVersion
import io.github.zenhelix.github.client.http.GithubConstants.APPLICATION_GITHUB_JSON_MEDIA_TYPE
import io.github.zenhelix.github.client.http.GithubConstants.GITHUB_API_VERSION_HEADER_NAME
import io.github.zenhelix.github.client.http.model.License
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.SimpleRequestExpectationManager
import org.springframework.test.web.client.match.MockRestRequestMatchers.header
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.client.RestClient
import kotlin.test.assertEquals

class GithubApiRestClientTest {

    private lateinit var mockServer: MockRestServiceServer
    private lateinit var client: GithubApiRestClient

    private companion object {
        private const val MOCK_TOKEN = "mock"
        private const val TEST_BASE_URL = "http://localhost"
    }

    @BeforeEach
    fun setUp() {
        val restClientBuilder = RestClient.builder()
        mockServer = MockRestServiceServer.bindTo(restClientBuilder).build(SimpleRequestExpectationManager())
        client = GithubApiRestClient(restClientBuilder, baseUrl = TEST_BASE_URL, defaultToken = MOCK_TOKEN)
    }

    @Test
    fun `success request`() {
        mockServer.expect(requestTo("$TEST_BASE_URL/licenses"))
            .andExpect(method(HttpMethod.GET))
            .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer $MOCK_TOKEN"))
            .andExpect(header(HttpHeaders.ACCEPT, "$APPLICATION_GITHUB_JSON_MEDIA_TYPE, $APPLICATION_JSON_VALUE"))
            .andExpect(header(GITHUB_API_VERSION_HEADER_NAME, GithubApiVersion.V_2022_11_28.version))
            .andRespond(
                withSuccess(
                    //language=JSON
                    """
                    [
                        {"key":"agpl-3.0","name":"GNU Affero General Public License v3.0","spdx_id":"AGPL-3.0","url":"https://api.github.com/licenses/agpl-3.0","node_id":"MDc6TGljZW5zZTE="},
                        {"key":"apache-2.0","name":"Apache License 2.0","spdx_id":"Apache-2.0","url":"https://api.github.com/licenses/apache-2.0","node_id":"MDc6TGljZW5zZTI="}
                    ]
                    """.trimIndent(),
                    MediaType.APPLICATION_JSON
                )
            )

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
            client.licenses()
        )

        mockServer.verify()
    }

}