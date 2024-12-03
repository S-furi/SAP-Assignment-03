package it.unibo.sap.ass02.infrastructure

import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import it.unibo.sap.ass02.domain.User
import it.unibo.sap.ass02.infrastructure.utils.JsonUtils
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class UserAPI : AbstractAPI<User, Int, Int>() {
    override suspend fun add(config: Int): Boolean =
        client
            .post(ServicesRoutes.USERS_ROUTE + "/new") {
                this.contentType(ContentType.Application.Json)
                this.setBody(JsonObject(mapOf("id" to JsonPrimitive(config), "credit" to JsonPrimitive(100))).toString())
            }.status.value == 200

    override suspend fun getAll(): List<User> =
        client.get(ServicesRoutes.USERS_ROUTE + "/all").let {
            JsonUtils.decodeHttpPayload<List<User>>(it)
                ?: listOf()
        }

    override suspend fun get(id: Int): User? =
        client.get("${ServicesRoutes.USERS_ROUTE}/$id").let {
            JsonUtils.decodeHttpPayload<User>(it)
        }
}
