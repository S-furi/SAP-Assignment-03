package it.unibo.sap.ass02.infrastructure.impl

import it.unibo.sap.ass02.domain.model.EBike
import it.unibo.sap.ass02.domain.model.P2d
import it.unibo.sap.ass02.domain.model.V2d
import it.unibo.sap.ass02.infrastructure.proxies.EbikeProxy

class EBikeImpl(
    override val id: String,
) : EBike {
    override var location: P2d
        get() = EbikeProxy.location(this.id)!!

    override var direction: V2d = V2d.fromCoord(1.0, 0.0)

    override var speed: Double = 0.0

    override var state: String
        get() = EbikeProxy.state(this.id)!!

    override var available: Boolean
        get() = EbikeProxy.isAvailable(this.id)!!

    override var battery: Int
        get() = EbikeProxy.battery(this.id)!!

    init {
        val b = EbikeProxy.getEBike(id) ?: throw IllegalArgumentException("Cannot retrieve EBIke with given id $id")
        this.location = b.location
        this.state = b.state
        this.available = b.available
        this.battery = b.battery
    }

    override fun updateSpeed(speed: Double) {
        this.speed = speed
    }

    override fun updateLocation(pos: P2d) {
        if (EbikeProxy.updateLocation(this.id, pos)) {
            this.location = pos
        }
    }

    override fun updateDirection(dir: V2d) {
        this.direction = dir
    }

    override fun drainBattery(delta: Int) {
        if (EbikeProxy.drainBattery(id, delta)) {
            this.battery -= delta
        }
    }
}
