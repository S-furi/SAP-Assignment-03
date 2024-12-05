package it.unibo.sap.ass02.infrastructure.proxies

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import it.unibo.sap.ass02.domain.Ride
import it.unibo.sap.ass02.domain.RideImpl
import it.unibo.sap.ass02.infrastructure.util.JsonUtils
import it.unibo.sap.ass02.infrastructure.util.LocalDateSerializer
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException
import java.time.LocalDate
import javax.naming.OperationNotSupportedException

object RideProxy : Proxy(
    healthcheckUri = "api/rides/health",
) {
    private val RIDE_ENDPOINT = "http://$gatewayHost:$gatewayPort/api/rides"
    private val RIDE_BY_ID = "$RIDE_ENDPOINT/"

    private fun startSimulationPath(id: Int) = "$RIDE_ENDPOINT/$id/start"

    private fun stopSimulationPath(id: Int) = "$RIDE_ENDPOINT/$id/stop"

    fun getRide(id: Int): Ride? =
        runBlocking {
            val res = client.get(RIDE_BY_ID + id)
            JsonUtils.decodeHttpPayload<RideDTO>(res)?.toRide()
        }

    fun startRide(id: Int): Boolean = this.manageRide(id, ::startSimulationPath)

    fun stopRide(id: Int): Boolean = this.manageRide(id, ::stopSimulationPath)

    private fun manageRide(
        id: Int,
        opPath: (Int) -> String,
    ) = runBlocking {
        runCatching {
            val res = client.post(opPath(id))

            if (res.status.value !in 200..299) {
                throw OperationNotSupportedException(
                    "The backend returned a non zero value for the required operation. Got: ${res.status.value}, ${res.bodyAsText()}",
                )
            }
            require(res.body<Int>() == 1)
        }.onFailure {
            when (it) {
                is NumberFormatException -> logger.error("Cannot parse response body... (which means it's an error). Got: ${it.message}")
                is IllegalArgumentException -> logger.error("Backend returned a value different from what expected.")
                is OperationNotSupportedException -> logger.error(it.message)
                else -> logger.error("Got ${it.message} from ride backend... \n${it.stackTraceToString()}")
            }
        }.isSuccess
    }

    @Serializable
    private data class RideDTO(
        val rideId: Int,
        val ebike: String,
        val user: Int,
        @Serializable(with = LocalDateSerializer::class) val endDate: LocalDate?,
        @Serializable(with = LocalDateSerializer::class) val startedDate: LocalDate?,
    ) {
        fun toRide(): Ride =
            RideImpl(
                id = rideId,
                userId = user,
                ebikeId = ebike,
                startedDate = startedDate,
                endDate = endDate,
            )
    }
}
