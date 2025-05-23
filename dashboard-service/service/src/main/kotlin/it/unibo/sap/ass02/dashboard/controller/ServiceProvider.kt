package it.unibo.sap.ass02.dashboard.controller

import it.unibo.sap.ass02.dashboard.presentation.EBikeAppView
import it.unibo.sap.ass02.dashboard.presentation.RideViewListener
import it.unibo.sap.ass02.domain.EBike
import it.unibo.sap.ass02.domain.P2d
import it.unibo.sap.ass02.domain.User
import it.unibo.sap.ass02.infrastructure.AbstractAPI
import it.unibo.sap.ass02.infrastructure.AsyncRideServiceAPI
import it.unibo.sap.ass02.infrastructure.EBikeAPI
import it.unibo.sap.ass02.infrastructure.UserAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import javax.swing.SwingUtilities

object ServiceProvider {
    private val logger = LoggerFactory.getLogger(ServiceProvider::class.java)
    private val dashboard = EBikeAppView()

    suspend fun display() {
        CoroutineScope(Dispatchers.IO)
            .launch {
                async { EBikeService.getAll() }.await().forEach(dashboard::notifiedEBikeAdded)
                async { UserService.getAll() }.await().forEach(dashboard::notifiedUserAdded)
            }.join()

        SwingUtilities.invokeLater {
            this.dashboard.display()
        }
    }

    object EBikeService : AsyncObserver<Map<String, Any>>, Service<EBike, Map<String, Any>, String>(EBikeAPI()) {
        override suspend fun notifiedUpdateRequested(update: Map<String, Any>) {
            updateRequested(update, apis::add).join()
            val ebikeId = update["id"]!! as String
            val x = update["x"]!! as Double
            val y = update["y"]!! as Double
            dashboard.notifiedEBikeAdded(EBike(ebikeId, P2d(x, y), true, 100))
        }
    }

    object UserService : AsyncObserver<Int>, Service<User, Int, Int>(UserAPI()) {
        override suspend fun notifiedUpdateRequested(update: Int) {
            updateRequested(update, apis::add).join()
            dashboard.notifiedUserAdded(User(update, 1000))
        }
    }

    object RideService : AsyncObserver<RideCommand>, RideViewListener {
        private val apis = AsyncRideServiceAPI
        private val scope = CoroutineScope(Dispatchers.IO)

        override suspend fun notifiedUpdateRequested(update: RideCommand) {
            val userId = update.userId
            val ebikeId = update.ebikeId
            when (update.command) {
                RideCommand.Command.START -> apis.startRide(userId, ebikeId)
                RideCommand.Command.STOP -> apis.stopRide(userId, ebikeId)
                RideCommand.Command.SUBSCRIBE -> return handleSubscription(userId, ebikeId)
            }
            dashboard.refresh()
        }

        private suspend fun handleSubscription(
            userId: Int,
            ebikeId: String,
        ) {
            logger.debug("Subscribed to simulation")
            apis.subscribeToSimulation(userId, ebikeId).collect {
                logger.debug("Ride status: {}", it)
                dashboard.notifiedRideUpdate(it.toRide())
            }
        }

        suspend fun getRideFromUserIdAndBikeId(
            userId: Int,
            ebikeId: String,
        ) = apis.getRideFromUserAndEBike(userId, ebikeId)

        override fun startRide(
            userId: Int,
            bikeId: String,
        ) {
            scope.launch {
                AsyncRideServiceAPI.subscribeToSimulation(userId, bikeId).collect { }
            }
        }

        override fun stopRide(rideId: Int) {
            scope.launch {
                AsyncRideServiceAPI.stopRide(rideId)
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
    }

    abstract class Service<T, C, I>(
        val apis: AbstractAPI<T, C, I>,
    ) {
        suspend fun getAll(): List<T> = apis.getAll()

        suspend fun find(id: I): T? = apis.get(id)

        suspend fun updateRequested(
            update: C,
            func: suspend (C) -> Unit,
        ) = coroutineScope {
            launch {
                func(update)
            }
        }
    }
}
