package it.unibo.sap.ass02.infrastructure

import it.unibo.sap.ass02.domain.EBike
import it.unibo.sap.ass02.domain.Ride
import it.unibo.sap.ass02.domain.User
import kotlinx.coroutines.flow.Flow

interface RideServiceAPI {
    suspend fun startRide(userId: Int, bikeId: String): Int?
    suspend fun stopRide(rideId: Int): Int?
    suspend fun stopAll()
    suspend fun subscribeToSimulation(rideId: Int, rateIntervalMillis: Long = 100): Flow<Ride>
    suspend fun getBikes(): List<EBike>
    suspend fun getBike(bikeId: String): EBike?
    suspend fun getUsers(): List<User>
    suspend fun getUser(userId: Int): User?
}
