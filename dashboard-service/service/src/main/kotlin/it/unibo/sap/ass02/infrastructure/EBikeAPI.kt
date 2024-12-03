package it.unibo.sap.ass02.infrastructure

import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import it.unibo.sap.ass02.domain.EBike
import it.unibo.sap.ass02.infrastructure.utils.JsonUtils
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class EBikeAPI : AbstractAPI<EBike, Map<String, Any>, String>() {
    override suspend fun getAll(): List<EBike> =
        client.get(ServicesRoutes.EBIKE_ROUTE + "/all").let {
            JsonUtils.decodeHttpPayload<List<EBike>>(it) ?: listOf()
        }

    override suspend fun add(config: Map<String, Any>): Boolean =
        client
            .post("${ServicesRoutes.EBIKE_ROUTE}/create") {
                this.contentType(ContentType.Application.Json)
                this.setBody(bikeMapToJson(config))
            }.also{
                println("post: ebike/create -> GOT: ${it.status.value}, with message: ${it.bodyAsText()}")
            }.status.value == 0

    override suspend fun get(id: String): EBike? =
        client.get("${ServicesRoutes.EBIKE_ROUTE}/$id").let {
            JsonUtils.decodeHttpPayload<EBike>(it)
        }

    private fun bikeMapToJson(bikeMap: Map<String, Any>): String {
        val id = bikeMap["id"] as? String
        val xCoord = bikeMap["x"] as? Double
        val yCoord = bikeMap["y"] as? Double

        return JsonObject(
            mapOf(
                "id" to JsonPrimitive(id),
                "location" to
                    JsonObject(
                        mapOf(
                            "x" to JsonPrimitive(xCoord),
                            "y" to JsonPrimitive(yCoord),
                        ),
                    ),
                "available" to JsonPrimitive(true),
                "state" to JsonPrimitive("AVAILABLE"),
                "speed" to JsonPrimitive(0.0),
                "battery" to JsonPrimitive(100),
            ),
        ).toString()
    }
}
