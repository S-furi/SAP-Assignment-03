package it.unibo.sap.ass02.domain

import it.unibo.sap.ass02.domain.ddd.ValueObject
import it.unibo.sap.ass02.domain.model.User

interface RideSimulation : ValueObject {
    val ride: Ride
    val user: User

    fun startSimulation()

    fun stopSimulation()

    companion object {
        fun newSimulation(
            ride: Ride,
            user: User,
        ) = RideSimulationImpl(ride, user)
    }
}
