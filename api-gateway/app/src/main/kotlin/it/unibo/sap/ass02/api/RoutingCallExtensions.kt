package it.unibo.sap.ass02.api

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import it.unibo.sap.ass02.CircuitBreakerConfiguration
import it.unibo.sap.ass02.CircuitBreakerConfiguration.handleRequest
import it.unibo.sap.ass02.CircuitBreakerConfiguration.logMetrics
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets

object RoutingCallExtensions {
    private val logger = LoggerFactory.getLogger(ApiGateway::class.java)

    suspend fun RoutingCall.handleBasicGet(
        serviceUri: String,
        prefix: String? = null,
    ) {
        val uri = this.extractAndConcatenateURI(serviceUri, prefix)
        logger.debug(uri)
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

    private fun RoutingCall.extractAndConcatenateURI(
        servicePath: String,
        prefix: String?,
    ): String {
        val apiRoute =
            this.parameters.getAll("param")?.joinToString("/")?.let {
                if (prefix != null) "$prefix/$it" else it
            } ?: ""
        return "$servicePath/$apiRoute"
    }

    fun RoutingCall.callWithCircuitBreaker(
        endpoint: String,
        method: HttpMethod,
    ): Result<HttpResponse> =
        runCatching {
            runBlocking {
                handleRequest(endpoint, method).also {
                    CircuitBreakerConfiguration.circuitBreaker.logMetrics()
                }
            }
        }
}
