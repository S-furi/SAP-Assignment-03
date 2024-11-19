package it.unibo.sap.ass02.service.routing

import it.unibo.sap.ass02.domain.Ride
import it.unibo.sap.ass02.domain.RideImpl
import it.unibo.sap.ass02.domain.model.EBike
import it.unibo.sap.ass02.domain.model.User

object RideSimulationResolver {
    private val registry = mutableMapOf<Int, Ride>()
    private var id = 0

    suspend fun createRide(
        bikeId: Int,
        userId: Int,
    ): Int? = TODO()

    private suspend fun createActualRide(
        bike: EBike,
        user: User,
    ) = RideImpl(bike, user, id = ++id).also { registry[it.id] = it }

    suspend fun deleteRide(id: Int) = registry.remove(id)?.id

    suspend fun findRide(id: Int) = registry[id]

    suspend fun startRide(id: Int) = registry[id]?.also { it.start() }

    suspend fun stopRide(id: Int) = registry[id]?.also { it.end() }
}
