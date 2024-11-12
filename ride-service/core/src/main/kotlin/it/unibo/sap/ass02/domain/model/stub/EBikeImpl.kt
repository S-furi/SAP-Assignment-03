package it.unibo.sap.ass02.domain.model.stub

import it.unibo.sap.ass02.domain.model.EBike
import it.unibo.sap.ass02.domain.model.P2d
import it.unibo.sap.ass02.domain.model.V2d

/**
 * TODO: implement here circuit breaker in case BikeAPI doesn't work and no results are yielded (do not use`!!`)
 */
class EBikeImpl(
    override val id: String,
) : EBike {
    override var location: P2d = EBikeStub.location(this.id)!!
        get() = EBikeStub.location(this.id)!!
    override var direction: V2d = EBikeStub.direction(this.id)!!
        get() = EBikeStub.direction(this.id)!!
    override var speed: Double = EBikeStub.speed(this.id)!!
        get() = EBikeStub.speed(this.id)!!
    override var state: String = EBikeStub.state(this.id)!!
        get() = EBikeStub.state(this.id)!!
    override var available: Boolean = EBikeStub.isAvailable(this.id)!!
        get() = EBikeStub.isAvailable(this.id)!!
    override var battery: Int = EBikeStub.battery(this.id)!!
        get() = EBikeStub.battery(this.id)!!

    override fun updateSpeed(speed: Double) {
        if (EBikeStub.updateBike(this.copy(speed = speed))) {
            this.speed = speed
        }
    }

    override fun updateLocation(pos: P2d) {
        if (EBikeStub.updateBike(this.copy(location = pos))) {
            this.location = pos
        }
    }

    override fun updateDirection(dir: V2d) {
        if (EBikeStub.updateBike(this.copy(direction = dir))) {
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
