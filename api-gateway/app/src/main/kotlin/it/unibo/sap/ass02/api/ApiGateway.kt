package it.unibo.sap.ass02.api

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.cio.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import it.unibo.sap.ass02.CircuitBreakerConfiguration
import it.unibo.sap.ass02.api.ApiRoutes.RIDE_ROUTES
import it.unibo.sap.ass02.api.ApiRoutes.USER_ROUTES
import it.unibo.sap.ass02.api.ApiRoutes.VEHICLE_ROUTES
import it.unibo.sap.ass02.api.ServiceName.RIDE_SERVICE
import it.unibo.sap.ass02.api.ServiceName.USER_SERVICE
import it.unibo.sap.ass02.api.ServiceName.VEHICLE_SERVICE
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

object ApiGateway {
    private val logger = LoggerFactory.getLogger(ApiGateway::class.java)

    fun Route.apiGateway() {
        get(VEHICLE_ROUTES) {
            call
                .callWithCircuitBreaker(VEHICLE_SERVICE + VEHICLE_ROUTES, HttpMethod.Get)
                ?.let {
                    call.respond(it.status, it.body())
                } ?: call.respond(HttpStatusCode.BadRequest, "")
        }
        get(USER_ROUTES) {
            call.callWithCircuitBreaker(USER_SERVICE + USER_ROUTES + call.parameters["param"], HttpMethod.Get)
        }
        get(RIDE_ROUTES) {
            call.callWithCircuitBreaker(RIDE_SERVICE + RIDE_ROUTES, HttpMethod.Get)
        }
        post(VEHICLE_ROUTES) {
            call.callWithCircuitBreaker(VEHICLE_SERVICE + VEHICLE_ROUTES, HttpMethod.Post)
        }
        post(USER_ROUTES) {
            call.callWithCircuitBreaker(USER_SERVICE + USER_ROUTES, HttpMethod.Post)
        }
        post(RIDE_ROUTES) {
            call.callWithCircuitBreaker(RIDE_SERVICE + RIDE_ROUTES, HttpMethod.Post)
        }
    }

    private fun RoutingCall.callWithCircuitBreaker(
        endpoint: String,
        method: HttpMethod,
    ): HttpResponse? =
        runCatching {
            CircuitBreakerConfiguration.circuitBreaker.executeCallable {
                runBlocking {
                    CircuitBreakerConfiguration.client.request(endpoint) {
                        this.method = method
                    }
                }
            }
        }.getOrNull()
}
