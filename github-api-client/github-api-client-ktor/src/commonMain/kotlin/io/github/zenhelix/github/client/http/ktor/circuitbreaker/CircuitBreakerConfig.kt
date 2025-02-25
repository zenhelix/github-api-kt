package io.github.zenhelix.github.client.http.ktor.circuitbreaker

import io.ktor.client.statement.HttpResponse
import io.ktor.util.collections.ConcurrentMap
import kotlinx.datetime.Clock
import kotlinx.datetime.Clock.System
import kotlin.jvm.JvmInline
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@DslMarker
public annotation class CircuitBreakerDsl


@CircuitBreakerDsl
public class CircuitBreakerConfig {
    internal val circuitBreakers: ConcurrentMap<CircuitBreakerName, CircuitBreaker> = ConcurrentMap()
    internal var global: CircuitBreaker? = null

    public class CircuitBreakerBuilder {
        /**
         * How many failures are to be tolerated before the circuit moves to OPEN.
         */
        public var failureThreshold: Int = 3

        /**
         * How many failures are allowed in HALF_OPEN state before going back to OPEN.
         */
        public var halfOpenFailureThreshold: Int = 2

        /**
         * How long to wait before moving from OPEN to HALF_OPEN.
         */
        public var resetInterval: Duration = 1.seconds

        /**
         * Maximum number of concurrent requests allowed in HALF_OPEN state.
         */
        public var maxHalfOpenAttempts: Int = 5

        /**
         * What is considered a failure. Default is HttpResponse.status >= 400
         */
        public var failureTrigger: HttpResponse.() -> Boolean = { status.value >= 400 }

        internal fun build(): CircuitBreakerConfiguration = CircuitBreakerConfiguration(
            failureThreshold = failureThreshold,
            halfOpenFailureThreshold = halfOpenFailureThreshold,
            resetInterval = resetInterval,
            maxHalfOpenAttempts = maxHalfOpenAttempts,
            failureTrigger = failureTrigger
        )
    }

    @CircuitBreakerDsl
    public fun circuitBreaker(name: CircuitBreakerName, clock: Clock = System, builder: CircuitBreakerBuilder.() -> Unit = {}) {
        require(!circuitBreakers.containsKey(name)) { "Circuit Breaker with name $name is already registered" }
        val config = CircuitBreakerBuilder().apply(builder)
        circuitBreakers[name] = CircuitBreaker(name, clock = clock, config = config.build())
    }

    @CircuitBreakerDsl
    public fun global(clock: Clock = System, builder: CircuitBreakerBuilder.() -> Unit = {}) {
        val config = CircuitBreakerBuilder().apply(builder)
        global = CircuitBreaker(CIRCUIT_BREAKER_NAME_GLOBAL, clock = clock, config = config.build())
    }

}

@JvmInline
public value class CircuitBreakerName(public val value: String)