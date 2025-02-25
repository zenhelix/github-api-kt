package io.github.zenhelix.github.client.http.ktor.circuitbreaker

import io.github.zenhelix.github.client.http.ktor.circuitbreaker.CircuitBreakerState.CLOSED
import io.github.zenhelix.github.client.http.ktor.circuitbreaker.CircuitBreakerState.HALF_OPEN
import io.github.zenhelix.github.client.http.ktor.circuitbreaker.CircuitBreakerState.OPEN
import io.ktor.client.statement.HttpResponse
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import kotlinx.atomicfu.updateAndGet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Clock.System
import kotlinx.datetime.Instant
import kotlin.properties.Delegates
import kotlin.time.Duration

internal class CircuitBreaker(
    private val name: CircuitBreakerName,
    private val clock: Clock = System,
    private val config: CircuitBreakerConfiguration
) {
    private val failureTrigger = config.failureTrigger

    private val failureCounter = atomic(0)

    private val openTimestamp = atomic(MIN_INSTANT)

    private val _state = atomic(CLOSED)

    private var scope: CoroutineScope by Delegates.notNull()

    internal fun initialize(dispatcher: CoroutineDispatcher) {
        scope = CoroutineScope(dispatcher)
    }

    internal fun wire() {
        when (_state.value) {
            CLOSED, HALF_OPEN -> return
            OPEN -> throw CircuitBreakerException(config.failureThreshold, openTimestamp.value + config.resetInterval)
        }
    }

    internal fun handleResponse(response: HttpResponse) {
        when (val state = _state.value) {
            CLOSED, HALF_OPEN -> handleResponse(state, response)
            OPEN              -> error("Circuit breaker is already open")
        }
    }

    private fun handleResponse(state: CircuitBreakerState, response: HttpResponse) {
        val selectedFailureThreshold = when (state) {
            CLOSED    -> config.failureThreshold
            HALF_OPEN -> config.halfOpenFailureThreshold
            OPEN      -> error("Circuit breaker is already open")
        }
        val failureCount = failureCounter.value
        if (failureCount < selectedFailureThreshold - 1) {
            if (!response.failureTrigger()) {
                closeCircuit()
                return
            } else {
                failureCounter.updateAndGet { it + 1 }
            }
        } else {
            openCircuit()
        }
    }

    private fun openCircuit() {
        _state.update { OPEN }

        openTimestamp.update { clock.now() }

        scope.launch(CoroutineName("CircuitBreaker-$name-half-opener")) {
            delay(config.resetInterval)
            halfOpenCircuit()
        }
    }

    private fun halfOpenCircuit() {
        failureCounter.update { 0 }
        _state.update { HALF_OPEN }
        openTimestamp.update { MIN_INSTANT }
    }

    private fun closeCircuit() {
        failureCounter.update { 0 }
        _state.update { CLOSED }
        openTimestamp.update { MIN_INSTANT }
    }

    private companion object {
        private val MIN_INSTANT = Instant.fromEpochMilliseconds(0)
    }
}

internal enum class CircuitBreakerState {
    CLOSED, OPEN, HALF_OPEN
}

public class CircuitBreakerException(
    public val failureThreshold: Int, public val nextHalfOpenTime: Instant
) : Exception(
    "Action failed more than $failureThreshold times, subsequent calls will be prevented until action is successful again. Circuit will half-open in '$nextHalfOpenTime'"
)

internal data class CircuitBreakerConfiguration(
    val failureThreshold: Int,
    val halfOpenFailureThreshold: Int,
    val resetInterval: Duration,
    val failureTrigger: HttpResponse.() -> Boolean
)