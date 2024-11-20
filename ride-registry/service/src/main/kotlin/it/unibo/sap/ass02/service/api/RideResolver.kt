package it.unibo.sap.ass02.service.api

import it.unibo.sap.ass02.domain.Ride
import it.unibo.sap.ass02.domain.RideImpl
import it.unibo.sap.ass02.domain.model.EBikeImpl
import it.unibo.sap.ass02.domain.model.UserImpl
import it.unibo.sap.ass02.persistence.repository.RideRepository

object RideResolver {
    private val repository = RideRepository()

    suspend fun getAllRides(): List<Ride> = this.repository.findAll().toList()

    suspend fun getRideByID(id: Int): Ride? = this.repository.findByID(id)

    suspend fun addNewRide(
        bikeId: String,
        userId: Int,
    ): Int? =
        this.repository.save(
            RideImpl(
                id = this.repository.getLastId(),
                ebike = EBikeImpl(bikeId),
                user = UserImpl(userId),
            ),
        )

    suspend fun updateRide(ride: Ride): Int? = this.repository.update(ride)

    suspend fun deleteRide(id: Int): Int? = this.repository.delete(id)
}
