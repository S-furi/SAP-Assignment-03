package it.unibo.sap.ass02.dashboard.controller

import it.unibo.sap.ass02.dashboard.presentation.RideViewListener
import it.unibo.sap.ass02.infrastructure.AsyncRideServiceAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RideController : RideViewListener {
    private val logger: Logger = LoggerFactory.getLogger(RideController::class.java)

    override fun startRide(
        userId: Int,
        bikeId: String,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            AsyncRideServiceAPI.startRide(userId, bikeId)?.let {
                AsyncRideServiceAPI.subscribeToSimulation(it).collect { ride ->
//                        withContext(Dispatchers.Main) { view.notifiedRideUpdate(ride) }
                    logger.debug(ride.toString())
                }
            } ?: logger.error("Cannot create ride for parameters: (bikeId: $bikeId, userId: $userId)")
        }
    }

    override fun stopRide(rideId: Int) {
        // Maybe todo: cancel job created during startRide
        CoroutineScope(Dispatchers.IO).async { AsyncRideServiceAPI.stopRide(rideId) }
    }
}

fun main() =
    runBlocking {
        val rideController = RideController()
        rideController.startRide(123, "u32")
        delay(10000)
        rideController.stopRide(1)
    }
