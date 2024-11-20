package it.unibo.sap.ass02.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserImpl(
    override val id: Int,
) : User
