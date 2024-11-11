package it.unibo.sap.ass02.domain

import it.unibo.sap.ass02.domain.model.EBike
import it.unibo.sap.ass02.domain.model.User
import java.time.LocalDate

class RideImpl(
    override val ebike: EBike,
    override val user: User,
    override val startedDate: LocalDate,
    override val endDate: LocalDate,
    override val id: Int
) : Ride{
    override fun end() {
        TODO("Not yet implemented")
    }

}
