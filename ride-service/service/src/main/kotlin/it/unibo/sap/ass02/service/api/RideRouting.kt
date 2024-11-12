package it.unibo.sap.ass02.service.api

import io.ktor.server.response.*
import io.ktor.server.routing.*
import it.unibo.sap.ass02.service.api.RideRoutes.ALL_RIDES

object RideRouting {
    fun Route.rideRouting() {
        get(ALL_RIDES) {
            call.respond(RideResolver.getAllRides())
        }
    }
}
