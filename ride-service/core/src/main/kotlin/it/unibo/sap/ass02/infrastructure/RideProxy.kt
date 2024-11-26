package it.unibo.sap.ass02.infrastructure

import io.ktor.client.call.body
import io.ktor.client.request.get
import it.unibo.sap.ass02.domain.Ride
import it.unibo.sap.ass02.domain.RideImpl
import it.unibo.sap.ass02.infrastructure.util.JsonUtils
import it.unibo.sap.ass02.infrastructure.util.LocalDateSerializer
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import java.time.LocalDate

data object RideProxy : Proxy(
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
            require(client.get(opPath(id)).body<Int>() == 1)
        }.onFailure {
            logger.error("Got ${it.message} from ride backend...")
        }.isSuccess
    }

    @Serializable
    private data class RideDTO(
        val id: Int,
        val ebike: RideEBikeDTO,
        val user: RideUserDTO,
        @Serializable(with = LocalDateSerializer::class) val endDate: LocalDate?,
        @Serializable(with = LocalDateSerializer::class) val startDate: LocalDate?,
    ) {
        fun toRide(): Ride =
            RideImpl(
                id = id,
                userId = user.id,
                ebikeId = ebike.id,
                startedDate = startDate,
                endDate = endDate,
            )
    }

    @Serializable
    private data class RideEBikeDTO(
        val id: String,
    )

    @Serializable
    private data class RideUserDTO(
        val id: Int,
    )
}
