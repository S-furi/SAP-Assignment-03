package it.unibo.sap.ass02.api

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readReason
import io.ktor.websocket.readText
import it.unibo.sap.ass02.GatewayCircuitBreaker
import it.unibo.sap.ass02.GatewayCircuitBreaker.handleRequest
import it.unibo.sap.ass02.GatewayCircuitBreaker.logMetrics
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
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

    private fun ApplicationCall.extractAndConcatenateURI(
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
                    GatewayCircuitBreaker.circuitBreaker.logMetrics()
                }
            }
        }

    suspend fun proxyWSRequest(
        clientSession: DefaultWebSocketServerSession,
        backendWSUri: String,
    ): Result<Any> =
        runCatching {
            val backendSession = GatewayCircuitBreaker.createWebSocketSession(backendWSUri)
            coroutineScope {
                launch { backendSession.forwardFrames(clientSession) }
                launch { clientSession.forwardFrames(backendSession) }
            }
        }.onFailure {
            logger.error(it.message)
            clientSession.close()
        }

    private suspend fun WebSocketSession.forwardFrames(dest: WebSocketSession) =
        this.incoming.consumeEach {
            when (it) {
                is Frame.Text -> dest.send(Frame.Text(it.readText()))
                is Frame.Binary -> dest.send(Frame.Binary(true, it.data))
                is Frame.Close -> dest.send(it.readReason()?.let { reason -> Frame.Close(reason) } ?: Frame.Close())
                else -> logger.warn("WS frame not recognized, got $it")
            }
        }
}
