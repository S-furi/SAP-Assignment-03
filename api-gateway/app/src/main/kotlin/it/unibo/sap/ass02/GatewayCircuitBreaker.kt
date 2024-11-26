package it.unibo.sap.ass02

import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.kotlin.circuitbreaker.executeSuspendFunction
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import org.slf4j.LoggerFactory

object GatewayCircuitBreaker {
    private val logger = LoggerFactory.getLogger(CircuitBreakerConfig::class.java)
    private val client = HttpClient(CIO)

    val circuitBreaker = CircuitBreaker.ofDefaults("API-Gateway")

    suspend fun handleRequest(
        endpoint: String,
        method: HttpMethod,
    ): HttpResponse =
        circuitBreaker.executeSuspendFunction {
            client.request(endpoint) {
                this.method = method
            }
        }

    suspend fun createWebSocketSession(uri: String) = this.client.webSocketSession(uri)

    fun CircuitBreaker.logMetrics() {
        logger.debug(
            """
            failureRate: ${this.metrics.failureRate}
            numberOfFailedCalls: ${this.metrics.numberOfFailedCalls}
            numberOfSlowFailedCalls: ${this.metrics.numberOfSlowFailedCalls}
            numberOfSuccessfulCalls: ${this.metrics.numberOfSuccessfulCalls}
            numberOfSlowSuccessfulCalls: ${this.metrics.numberOfSlowSuccessfulCalls}
            numberOfNotPermittedCalls; ${this.metrics.numberOfNotPermittedCalls}
            numberOfBufferedCalls: ${this.metrics.numberOfBufferedCalls}
            numberOfSlowCalls: ${this.metrics.numberOfSlowCalls}
            """.trimIndent(),
        )
    }
}
