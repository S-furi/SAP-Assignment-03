package it.unibo.sap.ass02.infrastructure.proxies

import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import it.unibo.sap.ass02.domain.model.EBike
import it.unibo.sap.ass02.domain.model.P2d
import it.unibo.sap.ass02.domain.model.V2d
import it.unibo.sap.ass02.infrastructure.proxies.EBikeRoutes.GET_EBIKE_BY_ID
import it.unibo.sap.ass02.infrastructure.proxies.EBikeRoutes.UPDATE_BATTERY_ENDPOINT
import it.unibo.sap.ass02.infrastructure.proxies.EBikeRoutes.UPDATE_LOCATION_ENDPOINT
import it.unibo.sap.ass02.infrastructure.proxies.EBikeRoutes.VEHICLE_HEALTHCHECK
import it.unibo.sap.ass02.infrastructure.util.JsonUtils
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import java.nio.charset.StandardCharsets

object EbikeProxy : Proxy(
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
                    parameters.append("y", p.y.toString())
                }
            }.also {
                logger.debug(
                    """
                    code: ${it.status.value}
                    message: ${it.bodyAsText(StandardCharsets.UTF_8)}
                    """.trimIndent(),
                )
            }.checkStatusCode()
    }

    fun drainBattery(
        id: String,
        delta: Int,
    ): Boolean =
        runBlocking {
            client
                .put(UPDATE_BATTERY_ENDPOINT + id) {
                    url {
                        parameters.append("delta", delta.toString())
                    }
                }.checkStatusCode()
        }

    private fun HttpResponse.checkStatusCode(errMsg: (HttpResponse) -> String = { "An error occurred" }) =
        (this.status.value in 200..299).also {
            if (!it) logger.error("An error occurred")
        }

    fun location(id: String): P2d? = this.getEBike(id)?.location

    fun state(id: String): String? = this.getEBike(id)?.state

    fun isAvailable(id: String): Boolean? = this.getEBike(id)?.available

    fun battery(id: String): Int? = this.getEBike(id)?.battery

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

        override fun drainBattery(delta: Int) {
            if (EbikeProxy.drainBattery(id, delta)) {
                this.battery -= delta
            }
        }

        override fun updateDirection(dir: V2d) {
            this.direction = dir
        }
    }
}

object EBikeRoutes {
    private val VEHICLE_ENDPOINT = "http://${EbikeProxy.gatewayHost}:${EbikeProxy.gatewayPort}/api/vehicles/ebike"
    const val VEHICLE_HEALTHCHECK = "api/vehicles/actuator/health"
    val UPDATE_LOCATION_ENDPOINT = "$VEHICLE_ENDPOINT/update/location/"
    val UPDATE_BATTERY_ENDPOINT = "$VEHICLE_ENDPOINT/update/battery/"
    val GET_EBIKE_BY_ID = "$VEHICLE_ENDPOINT/"
}
