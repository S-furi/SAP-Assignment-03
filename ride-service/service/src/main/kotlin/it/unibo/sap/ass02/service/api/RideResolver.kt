package it.unibo.sap.ass02.service.api

import it.unibo.sap.ass02.domain.Ride
import it.unibo.sap.ass02.persistence.repository.RideRepository

object  RideResolver {
    private val repository = RideRepository()
    suspend fun getAllRides(): Iterable<Ride> = this.repository.findAll()
    suspend fun getRideByID(id: Int) : Ride? = this.repository.findByID(id)
    suspend fun addNewRide(ride: Ride): Int? = this.repository.save(ride)
    suspend fun updateRide(ride: Ride): Int = this.repository.update(ride)
    suspend fun deleteRide(id: Int): Unit = print("TODO")

}
