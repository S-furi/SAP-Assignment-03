package it.unibo.sap.ass02.service.routing

import it.unibo.sap.ass02.domain.Ride
import it.unibo.sap.ass02.domain.RideImpl
import it.unibo.sap.ass02.domain.model.EBike
import it.unibo.sap.ass02.domain.model.EBikeImpl
import it.unibo.sap.ass02.domain.model.User
import it.unibo.sap.ass02.domain.model.UserImpl

object RideSimulationResolver {
    private val registry = mutableMapOf<Int, Ride>()
    private var id = 0

    suspend fun createRide(
        bikeId: String,
        userId: Int,
    ): Int? =
        createActualRide(
            EBikeImpl(bikeId),
            UserImpl(userId),
        )?.id

    private suspend fun createActualRide(
        bike: EBike,
        user: User,
    ): Ride? =
        runCatching {
            val ebike = RideImpl(bike, user, id = ++id)
            this.registry[ebike.id] = ebike
            ebike
        }.getOrNull()

    suspend fun deleteRide(id: Int) = registry.remove(id)?.id

    suspend fun findRide(id: Int) = registry[id]

    suspend fun startRide(id: Int) = registry[id]?.also { it.start() }

    suspend fun stopRide(id: Int) = registry[id]?.also { it.end() }
}
