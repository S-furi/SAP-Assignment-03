package it.unibo.sap.ass02.domain.model

import it.unibo.sap.ass02.domain.ddd.Entity

interface EBike : Entity<String> {
    var location: P2d
    var direction: V2d
    var speed: Double
    var state: String // TODO: create an enum
    var available: Boolean
    var battery: Int

    fun updateSpeed(speed: Double)

    fun updateLocation(pos: P2d)

    fun updateDirection(dir: V2d)

    fun drainBattery(delta: Int = 1)
}
