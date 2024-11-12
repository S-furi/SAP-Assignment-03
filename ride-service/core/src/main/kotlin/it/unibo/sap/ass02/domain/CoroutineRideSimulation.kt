package it.unibo.sap.ass02.domain

import it.unibo.sap.ass02.domain.model.EBike
import it.unibo.sap.ass02.domain.model.P2d
import it.unibo.sap.ass02.domain.model.User
import it.unibo.sap.ass02.domain.model.V2d
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CoroutineRideSimulation(
    override val ride: Ride,
    override val user: User,
) : RideSimulation {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private lateinit var simulation: Job

    override fun startSimulation() {
        this.simulation =
            this.coroutineScope.launch {
                val bike = ride.ebike
                bike.updateSpeed(1.0)
                user.decreaseCredit(1)

                var lastTimeChangeDir = System.currentTimeMillis()
                var lastTimeDecreasedCredit = System.currentTimeMillis()

                while (isActive) {
                    updateBike(bike)
                    if (System.currentTimeMillis() - lastTimeChangeDir > 500) {
                        bike.updateDirection(bike.direction.rotate(Math.random() * 60 - 30))
                        lastTimeChangeDir = System.currentTimeMillis()
                    }

                    if (System.currentTimeMillis() - lastTimeDecreasedCredit > 1000) {
                        user.decreaseCredit(1)
                        lastTimeDecreasedCredit = System.currentTimeMillis()
                    }
                }

                // update stuff here like observers
                delay(20)
            }
    }

    private fun updateBike(bike: EBike) {
        val direction = bike.direction
        val speed = bike.speed
        var pos = bike.location
        bike.updateLocation(pos + (direction * speed.toInt()))
        pos = bike.location

        if (pos.x > 200 || pos.x < -200) {
            bike.updateDirection(V2d.fromCoord(-direction.x, direction.y))
            bike.updateLocation(P2d.fromCoord(if (pos.x > 200) 200 else -200, pos.y))
        }

        if (pos.y > 200 || pos.y < -200) {
            bike.updateDirection(V2d.fromCoord(direction.x, -direction.y))
            bike.updateLocation(P2d.fromCoord(pos.x, if (pos.y > 200) 200 else -200))
        }
    }

    override fun stopSimulation() {
        this.simulation.cancel()
        ride.end()
        // update model for one last time
        // February every four years: can you give one more day?
    }
}
