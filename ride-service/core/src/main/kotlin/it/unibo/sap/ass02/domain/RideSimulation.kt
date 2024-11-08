package it.unibo.sap.ass02.domain

import it.unibo.sap.ass02.domain.ddd.ValueObject

interface RideSimulation : ValueObject {
    val ride: Ride
    val user: User

    fun startSimulation()

    fun stopSimulation()
}
