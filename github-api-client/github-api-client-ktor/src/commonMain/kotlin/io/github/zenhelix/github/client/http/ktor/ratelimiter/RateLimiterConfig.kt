package io.github.zenhelix.github.client.http.ktor.ratelimiter

import io.github.zenhelix.github.client.http.ktor.utils.RateLimitLimit
import io.github.zenhelix.github.client.http.ktor.utils.RateLimitRemaining
import io.github.zenhelix.github.client.http.ktor.utils.RateLimitReset
import io.github.zenhelix.github.client.http.ktor.utils.RateLimitResource
import io.github.zenhelix.github.client.http.ktor.utils.RateLimitUsed
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.util.collections.ConcurrentMap
import kotlinx.datetime.Clock
import kotlinx.datetime.Clock.System
import kotlin.jvm.JvmInline
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@DslMarker
public annotation class RateLimiterDsl

@RateLimiterDsl
public class RateLimiterConfig {
    internal val rateLimiters: ConcurrentMap<RateLimiterName, RateLimiter> = ConcurrentMap()
    internal var global: RateLimiter? = null

    public class RateLimiterBuilder {
        public var limitHeader: String = HttpHeaders.RateLimitLimit
        public var remainingHeader: String = HttpHeaders.RateLimitRemaining
        public var resetHeader: String = HttpHeaders.RateLimitReset
        public var usedHeader: String = HttpHeaders.RateLimitUsed
        public var resourceHeader: String = HttpHeaders.RateLimitResource

        public var remainingThreshold: Int = 0
        public var defaultResetDelay: Duration = 60.seconds
        public var safetyMargin: Duration = 0.5.seconds

        public var rateLimitExceededTrigger: HttpResponse.() -> Boolean = { status == HttpStatusCode.TooManyRequests }

        internal fun build(): RateLimitConfiguration = RateLimitConfiguration(
            limitHeader = limitHeader,
            remainingHeader = remainingHeader,
            resetHeader = resetHeader,
            usedHeader = usedHeader,
            resourceHeader = resourceHeader,
            remainingThreshold = remainingThreshold,
            defaultResetDelay = defaultResetDelay,
            safetyMargin = safetyMargin,
            rateLimitExceededTrigger = rateLimitExceededTrigger
        )
    }

    @RateLimiterDsl
    public fun rateLimiter(name: RateLimiterName, clock: Clock = System, builder: RateLimiterBuilder.() -> Unit = {}) {
        require(!rateLimiters.containsKey(name)) { "Circuit Breaker with name $name is already registered" }
        val config = RateLimiterBuilder().apply(builder)
        rateLimiters[name] = RateLimiter(name, clock = clock, config = config.build())
    }

    @RateLimiterDsl
    public fun global(clock: Clock = System, builder: RateLimiterBuilder.() -> Unit = {}) {
        val config = RateLimiterBuilder().apply(builder)
        global = RateLimiter(RATE_LIMITER_NAME_GLOBAL, clock = clock, config = config.build())
    }

}

@JvmInline
public value class RateLimiterName(public val value: String)
