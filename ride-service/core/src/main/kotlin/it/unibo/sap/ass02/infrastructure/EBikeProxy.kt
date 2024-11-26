package it.unibo.sap.ass02.infrastructure

import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.statement.HttpResponse
import it.unibo.sap.ass02.domain.model.EBike
import it.unibo.sap.ass02.domain.model.P2d
import it.unibo.sap.ass02.domain.model.V2d
import it.unibo.sap.ass02.infrastructure.EBikeRoutes.GET_EBIKE_BY_ID
import it.unibo.sap.ass02.infrastructure.EBikeRoutes.UPDATE_LOCATION_ENDPOINT
import it.unibo.sap.ass02.infrastructure.EBikeRoutes.VEHICLE_HEALTHCHECK
import it.unibo.sap.ass02.infrastructure.util.JsonUtils
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

data object EbikeProxy : Proxy(
    healthcheckUri = VEHICLE_HEALTHCHECK,
) {
    fun getEBike(id: String): EBike? =
        runBlocking {
            val res = client.get(GET_EBIKE_BY_ID + id)
            JsonUtils.decodeHttpPayload<EBikeDTO>(res)?.toEBike()
        }

    fun updateLocation(
        id: String,
        p: P2d,
    ) = runBlocking {
        client
            .put(UPDATE_LOCATION_ENDPOINT + id) {
                url {
                    parameters.append("x", p.x.toString())
                    parameters.append("x", p.y.toString())
                }
            }.checkStatusCode()
    }

    private fun HttpResponse.checkStatusCode() =
        (this.status.value in 200..299).also {
            if (!it) logger.warn("An error occurred, got status code: $it")
        }

    @Serializable
    data class EBikeDTO(
        val id: String,
        val location: P2dDTO,
        val available: Boolean,
        val state: String,
        val speed: Double,
        val battery: Int,
    ) {
        fun toEBike() =
            EBikeImpl(
                this.id,
                P2d.fromCoord(this.location.x, this.location.y),
                V2d.fromCoord(1.0, 0.0),
                0.0,
                this.state,
                this.available,
                this.battery,
            )
    }

    @Serializable
    data class P2dDTO(
        val id: Int,
        val x: Double,
        val y: Double,
    )

    class EBikeImpl(
        override val id: String,
        override var location: P2d,
        override var direction: V2d,
        override var speed: Double,
        override var state: String,
        override var available: Boolean,
        override var battery: Int,
    ) : EBike {
        override fun updateSpeed(speed: Double) {
            this.speed = speed
        }

        override fun updateLocation(pos: P2d) {
            if (updateLocation(this.id, pos)) {
                this.location = pos
            }
        }

        override fun updateDirection(dir: V2d) {
            this.direction = dir
        }
    }
}

object EBikeRoutes {
    private val VEHICLE_ENDPOINT = "http://${EbikeProxy.gatewayHost}:${EbikeProxy.gatewayPort}/ebike"
    const val VEHICLE_HEALTHCHECK = "actuator/health"
    val UPDATE_LOCATION_ENDPOINT = "$VEHICLE_ENDPOINT/update/location"
    val GET_EBIKE_BY_ID = "$VEHICLE_ENDPOINT/"
}
