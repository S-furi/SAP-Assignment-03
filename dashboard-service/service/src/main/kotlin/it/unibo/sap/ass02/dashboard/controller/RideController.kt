package it.unibo.sap.ass02.dashboard.controller

import it.unibo.sap.ass02.dashboard.presentation.RideViewListener
import it.unibo.sap.ass02.infrastructure.AsyncRideServiceAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RideController : RideViewListener {
    private val logger: Logger = LoggerFactory.getLogger(RideController::class.java)
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun startRide(
        userId: Int,
        bikeId: String,
    ) {
        scope.launch {
            AsyncRideServiceAPI.subscribeToSimulation(userId, bikeId).collect { ride ->
//                        withContext(Dispatchers.Main) { view.notifiedRideUpdate(ride) }
                logger.debug(ride.toString())
            }
        }
    }

    override fun stopRide(
        userId: Int,
        ebikeId: String,
    ) {
        scope.launch {
            AsyncRideServiceAPI.stopRide(userId, ebikeId)
        }
    }

    override fun stopRide(rideId: Int) {
        scope.launch {
            AsyncRideServiceAPI.stopRide(rideId)
        }
    }
}

fun main(): Unit =
    runBlocking {
        val rideController = RideController()
        rideController.startRide(123, "u32")
        delay(10000)
        rideController.stopRide(123, "u32")
    }
