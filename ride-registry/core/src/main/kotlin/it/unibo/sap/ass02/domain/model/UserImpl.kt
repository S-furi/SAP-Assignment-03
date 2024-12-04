package it.unibo.sap.ass02.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserImpl(
    override val id: Int,
) : User {
    init {
        require(HttpRemoteEntity.checkId("users/$id")) {
            "Remote User with id: $id does not exists..."
        }
    }
}
