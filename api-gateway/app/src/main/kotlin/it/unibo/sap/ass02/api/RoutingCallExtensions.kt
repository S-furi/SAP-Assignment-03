package it.unibo.sap.ass02.api

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import it.unibo.sap.ass02.CircuitBreakerConfiguration
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets

object RoutingCallExtensions {
    private val logger = LoggerFactory.getLogger(ApiGateway::class.java)

    suspend fun RoutingCall.handleBasicGet(serviceUri: String) {
        val uri = this.extractAndConcatenateURI(serviceUri)
        this
            .callWithCircuitBreaker(uri, HttpMethod.Get)
            .onSuccess {
                this.respond(it.status, it.bodyAsText(StandardCharsets.UTF_8))
            }.onFailure {
                this.respond(HttpStatusCode.BadRequest, "Error: ${it.message}")
                logger.debug("calling route on: $uri")
                logger.error(it.stackTraceToString())
            }
    }

    private fun RoutingCall.extractAndConcatenateURI(servicePath: String) =
        "$servicePath/${this.parameters.getAll("param")?.joinToString("/") ?: ""}"

    fun RoutingCall.callWithCircuitBreaker(
        endpoint: String,
        method: HttpMethod,
    ): Result<HttpResponse> =
        runCatching {
            CircuitBreakerConfiguration.circuitBreaker.executeCallable {
                runBlocking {
                    CircuitBreakerConfiguration.client.request(endpoint) {
                        this.method = method
                    }
                }
            }
        }
}
