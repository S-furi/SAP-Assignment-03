package it.unibo.sap.ass02

import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.kotlin.circuitbreaker.executeSuspendFunction
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import org.slf4j.LoggerFactory

object GatewayCircuitBreaker {
    private val logger = LoggerFactory.getLogger(CircuitBreakerConfig::class.java)
    private val client =
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }

    val circuitBreaker = CircuitBreaker.ofDefaults("API-Gateway")

    suspend fun handleRequest(
        endpoint: String,
        method: HttpMethod,
        body: String? = null,
    ): HttpResponse =
        circuitBreaker.executeSuspendFunction {
            client.request(endpoint) {
                this.method = method
                body?.let {
                    this.setBody(it)
                    contentType(ContentType.Application.Json)
                }
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
