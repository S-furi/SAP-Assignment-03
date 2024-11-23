package it.unibo.sap.ass02.infrastructure.stub

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import it.unibo.sap.ass02.domain.model.EBike
import it.unibo.sap.ass02.domain.model.P2d
import it.unibo.sap.ass02.domain.model.V2d
import it.unibo.sap.ass02.infrastructure.impl.EBikeImpl
import it.unibo.sap.ass02.infrastructure.stub.EBikeRoutes.GET_EBIKE_BY_ID
import it.unibo.sap.ass02.infrastructure.stub.EBikeRoutes.UPDATE_EBIKE_ENDPOINT
import it.unibo.sap.ass02.infrastructure.stub.EBikeRoutes.UPDATE_LOCATION_ENDPOINT
import it.unibo.sap.ass02.infrastructure.stub.EBikeRoutes.VEHICLE_HEALTHCHECK
import it.unibo.sap.ass02.infrastructure.stub.EbikeProxy.EBikeDTO.Companion.toDTO
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

data object EbikeProxy : Proxy(
    healthcheckUri = VEHICLE_HEALTHCHECK,
) {
    private suspend fun retrieveEBike(id: String): EBikeDTO? {
        val res = client.get(GET_EBIKE_BY_ID + id)
        if (res.status.value in 200..299) {
            return res.body()
        }
        return null
    }

    fun getEBike(id: String): EBike? = runBlocking { retrieveEBike(id)?.toEBike() }

    fun updateBike(bike: EBike): Boolean =
        runBlocking {
            client
                .put(UPDATE_EBIKE_ENDPOINT + bike.id) {
                    contentType(ContentType.Application.Json)
                    setBody(bike.toDTO())
                }.checkStatusCode()
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

    fun location(id: String): P2d? =
        runBlocking {
            retrieveEBike(id)?.location?.let {
                P2d.fromCoord(it.x, it.y)
            }
        }

    fun direction(id: String): V2d? = V2d.fromCoord(1.0, 0.0)

    fun speed(id: String): Double? =
        runBlocking {
            retrieveEBike(id)?.speed
        }

    fun state(id: String): String? =
        runBlocking {
            retrieveEBike(id)?.state
        }

    fun isAvailable(id: String): Boolean? =
        runBlocking {
            retrieveEBike(id)?.available
        }

    fun battery(id: String): Int? =
        runBlocking {
            retrieveEBike(id)?.battery
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
        fun toEBike(): EBike =
            EBikeImpl(this.id).also {
                it.location = P2d.fromCoord(this.location.x, this.location.y)
                it.available = this.available
                it.state = this.state
                it.speed = this.speed
                it.battery = this.battery
            }

        companion object {
            fun EBike.toDTO() =
                EBikeDTO(
                    this.id,
                    P2dDTO(1, this.location.x, this.location.y),
                    this.available,
                    this.state,
                    this.speed,
                    this.battery,
                )
        }
    }

    @Serializable
    data class P2dDTO(
        val id: Int,
        val x: Double,
        val y: Double,
    )
}

object EBikeRoutes {
    private val VEHICLE_ENDPOINT = "http://${EbikeProxy.gatewayHost}:${EbikeProxy.gatewayPort}/ebike"
    const val VEHICLE_HEALTHCHECK = "actuator/health"
    val UPDATE_EBIKE_ENDPOINT = "$VEHICLE_ENDPOINT/update/"
    val UPDATE_LOCATION_ENDPOINT = "$VEHICLE_ENDPOINT/update/location"
    val GET_EBIKE_BY_ID = "$VEHICLE_ENDPOINT/"
}
