package it.unibo.sap.ass02.service.routing

import it.unibo.sap.ass02.domain.Ride
import it.unibo.sap.ass02.infrastructure.RideProxy
import org.slf4j.LoggerFactory

object RideSimulationResolver {
    private val logger = LoggerFactory.getLogger(RideSimulationResolver::class.java)

    private val registry = mutableMapOf<Int, Ride>()

    private fun retrieveRide(id: Int) =
        RideProxy.getRide(id).also {
            if (it == null) {
                logger.error("Cannot retrieve Ride!")
            }
        }

    fun findRide(id: Int): Ride? = registry[id] ?: retrieveRide(id)

    fun startRide(id: Int): Int? {
        if (registry[id] != null) {
            throw IllegalStateException("Ride with id $id has been already started!")
        }
        return retrieveRide(id)
            ?.start()
            ?.also {
                registry[it.id] = it
                logger.debug("RIde Found, setting it inside registry")
            }?.id
    }

    fun stopRide(id: Int): Int? =
        registry[id].also { if (it == null) logger.error("Ride with id $id has not been started yet!") else it.end() }?.id
}
