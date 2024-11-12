package it.unibo.sap.ass02.service.api

import it.unibo.sap.ass02.domain.Ride
import it.unibo.sap.ass02.persistence.repository.RideRepository

object  RideResolver {
    private val repository = RideRepository()
    suspend fun getAllRides(): Iterable<Ride> = this.repository.findAll()

}
