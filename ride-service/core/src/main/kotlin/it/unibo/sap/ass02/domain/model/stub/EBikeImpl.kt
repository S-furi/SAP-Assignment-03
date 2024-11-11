package it.unibo.sap.ass02.domain.model.stub

import it.unibo.sap.ass02.domain.model.EBike
import it.unibo.sap.ass02.domain.model.P2d
import it.unibo.sap.ass02.domain.model.V2d

class EBikeImpl(
    override val id: String,
) : EBike {
    override val position: P2d
        get() = TODO("Not yet implemented")
    override val direction: V2d
        get() = TODO("Not yet implemented")
    override val speed: Double
        get() = TODO("Not yet implemented")

    override fun updateSpeed(speed: Double) {
        TODO("Not yet implemented")
    }

    override fun updatePosition(pos: P2d) {
        TODO("Not yet implemented")
    }

    override fun updateDirection(dir: V2d) {
        TODO("Not yet implemented")
    }
}
