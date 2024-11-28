package it.unibo.sap.ass02.dashboard.presentation

import it.unibo.sap.ass02.domain.Ride

interface RideModelListener {
    fun notifiedRideUpdate(ride: Ride)
}
