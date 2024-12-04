package it.unibo.sap.ass02.service.modules

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import it.unibo.sap.ass02.service.routing.RideSimulationRouting.rideSimulation

fun Application.routes() {
    routing {
        rideSimulation()
    }
}