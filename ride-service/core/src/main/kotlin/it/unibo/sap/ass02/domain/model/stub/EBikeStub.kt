package it.unibo.sap.ass02.domain.model.stub

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import it.unibo.sap.ass02.domain.model.EBike
import it.unibo.sap.ass02.domain.model.P2d
import it.unibo.sap.ass02.domain.model.V2d
import it.unibo.sap.ass02.domain.model.stub.EBikeStub.EBikeDTO.Companion.toDTO
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

data object EBikeStub : Stub() {
    private val VEHICLE_ENDPOINT = "http://$GATEWAY_HOST:$GATEWAY_PORT/ebike"

    private val UPDATE_EBIKE_ENDPOINT = "$VEHICLE_ENDPOINT/update/"

    private val GET_EBIKE_BY_ID = "$VEHICLE_ENDPOINT/"

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
            val res =
                client.post(UPDATE_EBIKE_ENDPOINT + bike.id) {
                    contentType(ContentType.Application.Json)
                    setBody(bike.toDTO())
                }
            res.status.value in 200..299
        }

    fun location(id: String): P2d? =
        runBlocking {
            retrieveEBike(id)?.location?.let {
                P2d.fromCoord(it.x, it.y)
            }
        }

    fun direction(id: String): V2d? = null

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
                    P2dDTO(this.location.x, this.location.y),
                    this.available,
                    this.state,
                    this.speed,
                    this.battery,
                )
        }
    }

    @Serializable
    data class P2dDTO(
        val x: Int,
        val y: Int,
    )
}
