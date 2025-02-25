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
    private val attemptsInHalfOpen = atomic(0)
    private val openTimestamp = atomic(MIN_INSTANT)
    private val _state = atomic(CLOSED)

    private var scope: CoroutineScope by Delegates.notNull()

    internal fun initialize(dispatcher: CoroutineDispatcher) {
        scope = CoroutineScope(dispatcher)
    }

    internal fun wire() {
        when (_state.value) {
            CLOSED    -> return  // Normal operation, proceed
            HALF_OPEN -> {
                // In HALF_OPEN state, we allow a limited number of test requests
                // but don't want to overwhelm the service
                val attemptCount = attemptsInHalfOpen.incrementAndGet()
                if (attemptCount > config.maxHalfOpenAttempts) {
                    throw CircuitBreakerException(config.failureThreshold, openTimestamp.value + config.resetInterval)
                }
                return
            }

            OPEN      -> throw CircuitBreakerException(config.failureThreshold, openTimestamp.value + config.resetInterval)
        }
    }

    internal fun handleResponse(response: HttpResponse) {
        when (val state = _state.value) {
            CLOSED, HALF_OPEN -> handleResponse(state, response)
            OPEN -> {
                // This should not happen normally, but handle gracefully if it does
                if (!response.failureTrigger()) {
                    // If response is successful, move to half-open state
                    halfOpenCircuit()
                } else {
                    // Otherwise, keep circuit open and reset the timer
                    openCircuit()
                }
            }
        }
    }

    private fun handleResponse(state: CircuitBreakerState, response: HttpResponse) {
        val selectedFailureThreshold = when (state) {
            CLOSED    -> config.failureThreshold
            HALF_OPEN -> config.halfOpenFailureThreshold
            OPEN      -> error("Circuit breaker is already open")
        }

        // Reset attempt counter on successful response in HALF_OPEN state
        if (state == HALF_OPEN && !response.failureTrigger()) {
            closeCircuit()
            return
        }

        val failureCount = failureCounter.value
        if (!response.failureTrigger()) {
            // Success, return to CLOSED if we were in HALF_OPEN
            if (state == HALF_OPEN) {
                closeCircuit()
            }
            return
        } else {
            // Failure occurred
            if (failureCount < selectedFailureThreshold - 1) {
                failureCounter.updateAndGet { it + 1 }
            } else {
                openCircuit()
            }
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
        attemptsInHalfOpen.update { 0 }
        _state.update { HALF_OPEN }
        openTimestamp.update { MIN_INSTANT }
    }

    private fun closeCircuit() {
        failureCounter.update { 0 }
        attemptsInHalfOpen.update { 0 }
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
    val maxHalfOpenAttempts: Int,
    val failureTrigger: HttpResponse.() -> Boolean
)