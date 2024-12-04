package it.unibo.sap.ass02.domain

import it.unibo.sap.ass02.infrastructure.utils.JsonUtils
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class RideStatus(
    val rideId: Int,
    val ebikeId: String,
    val userId: Int,
    @Serializable(with = JsonUtils.LocalDateSerializer::class)
    val endDate: LocalDate?,
    @Serializable(with = JsonUtils.LocalDateSerializer::class)
    val startedDate: LocalDate?,
) {
    fun toRide(): Ride = Ride(rideId, ebikeId, userId, startedDate, endDate)
}
