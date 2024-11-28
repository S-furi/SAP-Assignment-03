package it.unibo.sap.ass02.domain

import kotlinx.serialization.Serializable

@Serializable
sealed interface Ride {
    val id: Int
    val bike: EBike
    val user: User
}
