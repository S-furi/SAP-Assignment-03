package it.unibolsap.ass02.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldNotBe
import it.unibo.sap.ass02.domain.RideImpl
import it.unibo.sap.ass02.domain.model.EBike
import it.unibo.sap.ass02.domain.model.P2d
import it.unibo.sap.ass02.domain.model.User
import it.unibo.sap.ass02.domain.model.V2d
import kotlinx.coroutines.delay

class RideTest :
    StringSpec({
        "A simulation should be runnable" {
            val b = DumbEBike("eb-1")
            val startingPosition = b.location
            val initialCredit = 1000

            val u = DumbUser(1, initialCredit)

            val ride =
                RideImpl(
                    ebike = b,
                    user = u,
                    id = 1,
                )

            ride.start()
            ride.startedDate.shouldNotBeNull()
            ride.endDate.shouldBeNull()

            delay(5000)

            ride.ebike.speed shouldNotBe 0.0
            ride.ebike.battery shouldNotBe 1000
            ride.ebike.location shouldNotBe startingPosition
            u.credit shouldBeLessThan initialCredit

            ride.end()
            ride.endDate.shouldNotBeNull()
        }
    })

data class DumbUser(
    override val id: Int,
    override var credit: Int,
) : User {
    override fun increaseCredit(amount: Int) {
        this.credit += amount
    }

    override fun decreaseCredit(amount: Int) {
        this.credit -= amount
    }
}

data class DumbEBike(
    override val id: String,
) : EBike {
    override var location: P2d = P2d.fromCoord(50, 50)
    override var direction: V2d = V2d.fromCoord(1.0, 1.0)
    override var speed: Double = 0.0
    override var state: String = "AVAILABLE"
    override var battery: Int = 1000

    override fun updateSpeed(speed: Double) {
        this.speed = speed
    }

    override fun updateLocation(pos: P2d) {
        this.location = pos
    }

    override fun updateDirection(dir: V2d) {
        this.direction = dir
    }

    override var available: Boolean = true
}