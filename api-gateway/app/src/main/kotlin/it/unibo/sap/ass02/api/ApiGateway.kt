package it.unibo.sap.ass02.api

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.websocket.webSocket
import it.unibo.sap.ass02.api.ApiRoutes.RIDE_ROUTES
import it.unibo.sap.ass02.api.ApiRoutes.USER_ROUTES
import it.unibo.sap.ass02.api.ApiRoutes.VEHICLE_ROUTES
import it.unibo.sap.ass02.api.RoutingCallExtensions.handleBasicGet
import it.unibo.sap.ass02.api.RoutingCallExtensions.handleBasicPost
import it.unibo.sap.ass02.api.RoutingCallExtensions.handleBasicPut
import it.unibo.sap.ass02.api.RoutingCallExtensions.proxyWSRequest
import it.unibo.sap.ass02.api.ServicesURIs.RIDE_REGISTRY
import it.unibo.sap.ass02.api.ServicesURIs.RIDE_SERVICE
import it.unibo.sap.ass02.api.ServicesURIs.USER_SERVICE
import it.unibo.sap.ass02.api.ServicesURIs.VEHICLE_SERVICE

object ApiGateway {
    fun Route.apiGateway() {
        get(VEHICLE_ROUTES) {
            call.handleBasicGet(VEHICLE_SERVICE)
        }
        get(USER_ROUTES) {
            call.handleBasicGet(USER_SERVICE, Prefixes.USER.prefix)
        }
        get(RIDE_ROUTES) {
            call.handleBasicGet(RIDE_REGISTRY, Prefixes.RIDE.prefix)
        }
        webSocket(RIDE_ROUTES) {
            call.parameters.getAll("param")?.joinToString("")?.let {
                proxyWSRequest(this, "$RIDE_SERVICE/$it")
            } ?: call.respond(HttpStatusCode.BadRequest, "Provided Ride Id was null...")
        }
        post(VEHICLE_ROUTES) {
            call.handleBasicPost(VEHICLE_SERVICE)
        }
        post(USER_ROUTES) {
            call.handleBasicPost(USER_SERVICE, Prefixes.USER.prefix)
        }
        post(RIDE_ROUTES) {
            call.handleBasicPost(RIDE_REGISTRY, Prefixes.RIDE.prefix)
        }
        put(VEHICLE_ROUTES) {
            call.handleBasicPut(VEHICLE_SERVICE)
        }
    }

    private enum class Prefixes(
        val prefix: String,
    ) {
        USER("users"),
        RIDE("rides"),
    }
}
