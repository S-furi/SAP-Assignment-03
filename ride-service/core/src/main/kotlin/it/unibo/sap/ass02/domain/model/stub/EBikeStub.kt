package it.unibo.sap.ass02.domain.model.stub

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
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

object EBikeStub {
    private val GATEWAY_HOST = System.getenv("GATEWAY_HOST") ?: "localhost"
    private val GATEWAY_PORT = System.getenv("GATEWAY_PORT") ?: 1926

    private val VEHICLE_ENDPOINT = "http://$GATEWAY_HOST:$GATEWAY_PORT/ebike"

    private val UPDATE_EBIKE_ENDPOINT = "$VEHICLE_ENDPOINT/update/"

    private val GET_EBIKE_BY_ID = "$VEHICLE_ENDPOINT/"

    private val client = HttpClient(CIO)

    private suspend fun getEBike(id: String): EBikeDTO? {
        val res = client.get(GET_EBIKE_BY_ID + id)
        if (res.status.value in 200..299) {
            return res.body()
        }
        return null
    }

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
            getEBike(id)?.location?.let {
                P2d.fromCoord(it.x, it.y)
            }
        }

    fun direction(id: String): V2d? = null

    fun speed(id: String): Double? =
        runBlocking {
            getEBike(id)?.speed
        }

    fun state(id: String): String? =
        runBlocking {
            getEBike(id)?.state
        }

    fun isAvailable(id: String): Boolean? =
        runBlocking {
            getEBike(id)?.available
        }

    fun battery(id: String): Int? =
        runBlocking {
            getEBike(id)?.battery
        }

    @Serializable
    private data class EBikeDTO(
        val id: String,
        val location: P2dDTO,
        val available: Boolean,
        val state: String,
        val speed: Double,
        val battery: Int,
    ) {
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
    private data class P2dDTO(
        val x: Int,
        val y: Int,
    )
}
