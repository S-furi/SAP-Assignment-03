package it.unibo.sap.ass02.domain

import it.unibo.sap.ass02.infrastructure.utils.JsonUtils
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
sealed interface Ride {
    val id: Int
    val bike: EBike
    val user: User
    val startedDate: LocalDate?
    val endDate: LocalDate?
}

@Serializable
data class RideImpl(
    override val id: Int,
    override val bike: EBike,
    override val user: User,
    @Serializable(with = JsonUtils.LocalDateSerializer::class)
    override val startedDate: LocalDate? = null,
    @Serializable(with = JsonUtils.LocalDateSerializer::class)
    override val endDate: LocalDate? = null,
) : Ride
