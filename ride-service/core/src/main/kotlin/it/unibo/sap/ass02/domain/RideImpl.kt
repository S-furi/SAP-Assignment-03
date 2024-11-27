package it.unibo.sap.ass02.domain

import it.unibo.sap.ass02.domain.model.EBike
import it.unibo.sap.ass02.domain.model.User
import it.unibo.sap.ass02.infrastructure.EbikeProxy
import it.unibo.sap.ass02.infrastructure.RideProxy
import it.unibo.sap.ass02.infrastructure.UserProxy
import java.time.LocalDate

data class RideImpl(
    private val ebikeId: String,
    private val userId: Int,
    override var startedDate: LocalDate? = null,
    override var endDate: LocalDate? = null,
    override val id: Int,
) : Ride {
    override val ebike: EBike =
        EbikeProxy.getEBike(ebikeId)
            ?: throw IllegalArgumentException("Bike with id: $ebikeId was not found")

    override val user: User =
        UserProxy.getUser(userId)
            ?: throw IllegalArgumentException("User with id: $userId was not found")

    private val simulation: RideSimulation = CoroutineRideSimulation(this, user)

    override fun start(): Ride? =
        this.takeIf { RideProxy.startRide(it.id) }?.also {
            this.simulation.startSimulation()
            this.startedDate = LocalDate.now()
        }

    override fun end(): Ride? =
        this.takeIf { RideProxy.stopRide(it.id) }?.also {
            this.simulation.stopSimulation()
            this.endDate = LocalDate.now()
        }
}
