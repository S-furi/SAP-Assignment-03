package it.unibo.sap.ass02.domain

import kotlinx.serialization.Serializable

@Serializable
data class EBike(
    val id: String,
    val location: P2d,
    val available: Boolean,
    val battery: Int,
) {
    override fun equals(other: Any?): Boolean = this.id == (other as? EBike)?.id
}
