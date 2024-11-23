package it.unibo.sap.ass02.api

import io.ktor.http.*
import io.ktor.server.routing.*
import it.unibo.sap.ass02.api.ApiRoutes.RIDE_ROUTES
import it.unibo.sap.ass02.api.ApiRoutes.USER_ROUTES
import it.unibo.sap.ass02.api.ApiRoutes.VEHICLE_ROUTES
import it.unibo.sap.ass02.api.RoutingCallExtensions.callWithCircuitBreaker
import it.unibo.sap.ass02.api.RoutingCallExtensions.handleBasicGet
import it.unibo.sap.ass02.api.ServicesURIs.RIDE_REGISTRY
import it.unibo.sap.ass02.api.ServicesURIs.USER_SERVICE
import it.unibo.sap.ass02.api.ServicesURIs.VEHICLE_SERVICE

object ApiGateway {
    fun Route.apiGateway() {
        get(VEHICLE_ROUTES) {
            call.handleBasicGet(VEHICLE_SERVICE)
        }
        get(USER_ROUTES) {
            call.handleBasicGet(USER_SERVICE, "users")
        }
        get(RIDE_ROUTES) {
            call.handleBasicGet(RIDE_REGISTRY)
        }
        post(VEHICLE_ROUTES) {
            call.callWithCircuitBreaker(VEHICLE_SERVICE + VEHICLE_ROUTES, HttpMethod.Post)
        }
        post(USER_ROUTES) {
            call.callWithCircuitBreaker(USER_SERVICE + USER_ROUTES, HttpMethod.Post)
        }
        post(RIDE_ROUTES) {
            call.callWithCircuitBreaker(RIDE_REGISTRY + RIDE_ROUTES, HttpMethod.Post)
        }
    }
}
