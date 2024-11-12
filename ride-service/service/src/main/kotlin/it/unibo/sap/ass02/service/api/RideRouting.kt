package it.unibo.sap.ass02.service.api

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import it.unibo.sap.ass02.service.api.RideRoutes.ALL_RIDES
import it.unibo.sap.ass02.service.api.RideRoutes.RIDE_BY_ID

object RideRouting {
    fun Route.rideRouting() {
        get(ALL_RIDES) {
            call.respond(RideResolver.getAllRides())
        }

        get(RIDE_BY_ID) {
            handleGetRideByID(call)
        }
    }

    private suspend fun handleGetRideByID(call: RoutingCall) {
        val rideID = call.parameters["id"]?.toInt()
        val message = "Error: the input Ride id does not exist or It is null."
        if (rideID == null) {
            call.respond(HttpStatusCode.BadRequest, message)
            return
        }else {
            RideResolver.getRideByID(rideID)?.let {
                call.respond(HttpStatusCode.OK, it)
            } ?: call.respond(HttpStatusCode.NotFound, message)
        }
    }
}
