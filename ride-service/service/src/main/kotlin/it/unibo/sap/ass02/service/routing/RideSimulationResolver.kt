package it.unibo.sap.ass02.service.routing

import it.unibo.sap.ass02.domain.Ride

object RideSimulationResolver {
    private val registry = mutableMapOf<Int, Ride>()

    fun findRide(id: Int) = registry[id]

    fun startRide(id: Int) = registry[id]?.also { it.start() }

    fun stopRide(id: Int) = registry[id]?.also { it.end() }
}
