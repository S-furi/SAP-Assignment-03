package it.unibo.sap.ass02.domain.model

import it.unibo.sap.ass02.domain.ddd.Entity
import kotlinx.serialization.Serializable

@Serializable
sealed interface EBike : Entity<String>
