package io.github.zenhelix.github.client.http.ktor.circuitbreaker

import io.github.zenhelix.github.client.http.ktor.circuitbreaker.CircuitBreakerState.HALF_OPEN
import io.github.zenhelix.github.client.http.ktor.circuitbreaker.CircuitBreakerState.OPEN
import io.ktor.client.statement.HttpResponse
import io.ktor.util.collections.ConcurrentMap
import kotlin.jvm.JvmInline
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@DslMarker
public annotation class CircuitBreakerDsl

/**
 * Configuration for [CircuitBreaker].
 */

@CircuitBreakerDsl
public class CircuitBreakerConfig {
    internal val circuitBreakers: ConcurrentMap<CircuitBreakerName, CircuitBreaker> = ConcurrentMap()
    internal var global: CircuitBreaker? = null

    public class CircuitBreakerBuilder {
        /**
         * How many failures are to be tolerated before the circuit moves to [HALF_OPEN].
         */
        public var failureThreshold: Int = 3

        /**
         * How many attempts are allowed in [HALF_OPEN] state.
         */
        public var halfOpenFailureThreshold: Int = 2

        /**
         * How long to wait before moving from [OPEN] to [HALF_OPEN].
         */
        public var resetInterval: Duration = 1.seconds

        /**
         * What is considered a failure. default is [HttpResponse.status] >= 400
         */
        public var failureTrigger: HttpResponse.() -> Boolean = { status.value >= 400 }

    }
}

/**
 * Value class for a [CircuitBreaker] name
 */
@JvmInline
public value class CircuitBreakerName(public val value: String) {
    public companion object {
        public fun String.toCircuitBreakerName() = CircuitBreakerName(this)
    }
}
