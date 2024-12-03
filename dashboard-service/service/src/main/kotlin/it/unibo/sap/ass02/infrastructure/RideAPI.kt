package it.unibo.sap.ass02.infrastructure

import it.unibo.sap.ass02.domain.RideStatus
import kotlinx.coroutines.flow.Flow

interface RideAPI {
    suspend fun startRide(
        userId: Int,
        bikeId: String,
    ): Int?

    suspend fun stopRide(rideId: Int): Int?

    suspend fun stopAll()

    suspend fun subscribeToSimulation(
        rideId: Int,
        rateIntervalMillis: Long = 100,
    ): Flow<RideStatus>
}
