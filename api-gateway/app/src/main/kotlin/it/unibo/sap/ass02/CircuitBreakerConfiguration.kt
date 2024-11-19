package it.unibo.sap.ass02

import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.ktor.client.*
import java.time.Duration

object CircuitBreakerConfiguration {
    private fun getConfiguration() = CircuitBreakerConfig.custom()
        .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
        .slidingWindowSize(10)
        .slowCallRateThreshold(70.0f)
        .failureRateThreshold(70.0f)
        .waitDurationInOpenState(Duration.ofSeconds(5))
        .slowCallDurationThreshold(Duration.ofNanos(3))
        .permittedNumberOfCallsInHalfOpenState(3)
        .build()
    val circuitBreaker = CircuitBreaker.of("API-Gateway", this.getConfiguration())
    val client = HttpClient()
}
