package it.unibo.sap.ass02.domain

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val credit: Int,
) {
    override fun equals(other: Any?): Boolean = this.id == (other as? User)?.id
}
