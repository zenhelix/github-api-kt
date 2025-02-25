package test

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.currentTime
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class TestClock(private val testScope: TestScope) : Clock {
    override fun now(): Instant = Instant.fromEpochMilliseconds(testScope.currentTime)
}

internal fun TestScope.clock(): Clock = TestClock(this)

internal fun TestScope.mockEngine(
    handler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData
) = MockEngine.create {
    this.dispatcher = StandardTestDispatcher(this@mockEngine.testScheduler)
    addHandler(handler)
}

internal fun TestScope.createMockEngine(block: MockEngineConfig.() -> Unit) = MockEngine.create {
    this.dispatcher = StandardTestDispatcher(this@createMockEngine.testScheduler)
    this.block()
}
