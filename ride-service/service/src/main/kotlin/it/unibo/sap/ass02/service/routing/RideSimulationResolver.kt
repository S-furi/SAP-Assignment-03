package it.unibo.sap.ass02.service.routing

import it.unibo.sap.ass02.domain.Ride
import it.unibo.sap.ass02.infrastructure.RideProxy

object RideSimulationResolver {
    private val registry = mutableMapOf<Int, Ride>()

    private fun retrieveRide(id: Int) = RideProxy.getRide(id)

    fun findRide(id: Int) = registry[id] ?: retrieveRide(id)

    fun startRide(id: Int): Int? {
        if (registry[id] != null) {
            throw IllegalStateException("Ride with id $id has been already started!")
        }
        return findRide(id)
            ?.start()
            ?.also {
                registry[it.id] = it
            }?.id
    }

    fun stopRide(id: Int): Int? =
        registry[id]?.also { it.end() }?.id
            ?: throw IllegalStateException("Ride with id $id has not been started yet!")
}
