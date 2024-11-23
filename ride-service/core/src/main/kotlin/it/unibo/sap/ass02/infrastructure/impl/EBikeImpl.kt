package it.unibo.sap.ass02.infrastructure.impl

import it.unibo.sap.ass02.domain.model.EBike
import it.unibo.sap.ass02.domain.model.P2d
import it.unibo.sap.ass02.domain.model.V2d
import it.unibo.sap.ass02.infrastructure.stub.EbikeProxy

class EBikeImpl(
    override val id: String,
) : EBike {
    override var location: P2d = EbikeProxy.location(this.id)!!
        get() = EbikeProxy.location(this.id)!!

    override var direction: V2d = EbikeProxy.direction(this.id)!!

    override var speed: Double = EbikeProxy.speed(this.id)!!
        get() = EbikeProxy.speed(this.id)!!
    override var state: String = EbikeProxy.state(this.id)!!
        get() = EbikeProxy.state(this.id)!!
    override var available: Boolean = EbikeProxy.isAvailable(this.id)!!
        get() = EbikeProxy.isAvailable(this.id)!!
    override var battery: Int = EbikeProxy.battery(this.id)!!
        get() = EbikeProxy.battery(this.id)!!

    override fun updateSpeed(speed: Double) {
        if (EbikeProxy.updateBike(this.copy(speed = speed))) {
            this.speed = speed
        }
    }

    override fun updateLocation(pos: P2d) {
        if (EbikeProxy.updateLocation(this.id, pos)) {
            this.location = pos
        }
    }

    override fun updateDirection(dir: V2d) {
        if (EbikeProxy.updateBike(this.copy(direction = dir))) {
            this.direction = dir
        }
    }

    private fun copy(
        location: P2d = this.location,
        direction: V2d = this.direction,
        speed: Double = this.speed,
        state: String = this.state,
        available: Boolean = this.available,
        battery: Int = this.battery,
    ): EBike {
        val b = EBikeImpl(this.id)
        b.location = location
        b.direction = direction
        b.speed = speed
        b.state = state
        b.available = available
        b.battery = battery
        return b
    }
}
