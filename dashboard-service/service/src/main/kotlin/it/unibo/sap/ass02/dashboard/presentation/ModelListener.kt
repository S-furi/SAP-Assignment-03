package it.unibo.sap.ass02.dashboard.presentation

import it.unibo.sap.ass02.domain.EBike
import it.unibo.sap.ass02.domain.Ride
import it.unibo.sap.ass02.domain.User

interface ModelListener {
    fun notifiedRideUpdate(ride: Ride)

    fun notifiedUserAdded(user: User)

    fun notifiedEBikeAdded(eBike: EBike)
}
