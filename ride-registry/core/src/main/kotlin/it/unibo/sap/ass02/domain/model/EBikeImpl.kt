package it.unibo.sap.ass02.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EBikeImpl(
    override val id: String,
) : EBike {
    init {
        require(HttpRemoteEntity.checkId("vehicles/ebike/$id")) {
            "Remote EBike with id: $id does not exists..."
        }
    }
}
