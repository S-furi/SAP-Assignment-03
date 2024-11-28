package it.unibo.sap.ass02.domain

import kotlinx.serialization.Serializable

@Serializable
sealed interface EBike {
    val id: String
    val location: P2d
}

@Serializable
data class EBikeImpl(
    override val id: String,
    override val location: P2dImpl,
    val available: Boolean,
    val battery: Int,
) : EBike
