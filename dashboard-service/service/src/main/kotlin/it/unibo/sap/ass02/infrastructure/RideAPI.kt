package it.unibo.sap.ass02.infrastructure

import it.unibo.sap.ass02.domain.RideStatus
import kotlinx.coroutines.flow.Flow

interface RideAPI {
    suspend fun startRide(
        userId: Int,
        bikeId: String,
    ): Int?

    suspend fun stopRide(
        userId: Int,
        ebikeId: String,
    )

    suspend fun stopRide(rideId: Int)

    suspend fun stopAll()

    /**
     * Creates and Subscribes to a NEW ride for given user and ebikeId.
     *
     * @param userId the user
     * @param ebikeId the ebike
     * @param rateIntervalMillis how fast (milliseconds) updates should occurr
     * @return a {{ Flow }} containing all {{ RideStatus }}
     */
    suspend fun subscribeToSimulation(
        userId: Int,
        ebikeId: String,
        rateIntervalMillis: Long = 20,
    ): Flow<RideStatus>

    /**
     * Subscribes to an existing ride.
     *
     * @param rideId the existing ride
     * @param rateIntervalMillis how fast (milliseconds) updates should occurr
     * @return a {{ Flow }} containing all {{ RideStatus }}
     */
    suspend fun subscribeToSimulation(
        rideId: Int,
        rateIntervalMillis: Long = 100,
    ): Flow<RideStatus>
}
