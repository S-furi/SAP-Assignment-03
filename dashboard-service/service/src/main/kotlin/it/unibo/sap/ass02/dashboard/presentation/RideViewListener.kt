package it.unibo.sap.ass02.dashboard.presentation

interface RideViewListener {
    fun startRide(
        userId: Int,
        bikeId: String,
    )

    fun stopRide(rideId: Int)

    fun stopRide(
        userId: Int,
        ebikeId: String,
    )
}
