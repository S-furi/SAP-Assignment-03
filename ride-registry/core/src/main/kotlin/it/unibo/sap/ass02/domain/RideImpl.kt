package it.unibo.sap.ass02.domain

import it.unibo.sap.ass02.domain.model.EBike
import it.unibo.sap.ass02.domain.model.User
import it.unibo.sap.ass02.utils.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
class RideImpl(
    override val ebike: EBike,
    override val user: User,
    @Serializable(with = LocalDateSerializer::class)
    override var startedDate: LocalDate? = null,
    @Serializable(with = LocalDateSerializer::class)
    override var endDate: LocalDate? = null,
    override val id: Int,
) : Ride {
    override fun start() {
        this.startedDate = LocalDate.now()
    }

    override fun end() {
        this.endDate = LocalDate.now()
    }
}
