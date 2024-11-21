package it.unibo.sap.ass02.domain.model

import kotlinx.serialization.Serializable

@Serializable
class EBikeImpl(
    override val id: String,
) : EBike {
    init {
        require(HttpRemoteEntity.checkId("http://localhost:1926/ebike/${this.id}")) {
            "Remote EBike with id: $id does not exists..."
        }
    }
}
