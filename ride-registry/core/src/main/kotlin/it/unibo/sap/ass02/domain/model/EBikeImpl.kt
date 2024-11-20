package it.unibo.sap.ass02.domain.model

import kotlinx.serialization.Serializable

@Serializable
class EBikeImpl(
    override val id: String,
) : EBike
