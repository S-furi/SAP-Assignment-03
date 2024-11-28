package it.unibo.sap.ass02.domain

import kotlinx.serialization.Serializable

@Serializable
sealed interface User {
    val id: Int
    val credit: Int
}

@Serializable
data class UserImpl(
    override val id: Int,
    override val credit: Int,
) : User
