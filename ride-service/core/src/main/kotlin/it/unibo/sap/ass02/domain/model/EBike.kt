package it.unibo.sap.ass02.domain.model

import it.unibo.sap.ass02.domain.ddd.Entity

interface EBike : Entity<String> {
    val location: P2d
    val direction: V2d
    val speed: Double

    fun updateSpeed(speed: Double)

    fun updateLocation(pos: P2d)

    fun updateDirection(dir: V2d)
}
