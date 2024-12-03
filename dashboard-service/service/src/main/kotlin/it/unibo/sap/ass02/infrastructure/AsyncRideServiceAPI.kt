package it.unibo.sap.ass02.infrastructure

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import it.unibo.sap.ass02.domain.EBike
import it.unibo.sap.ass02.domain.Ride
import it.unibo.sap.ass02.infrastructure.utils.JsonUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

object AsyncRideServiceAPI : RideAPI {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val jobs = mutableMapOf<Int, Job>()
    private val simulations = mutableMapOf<Int, MutableSharedFlow<Ride>>()
    private val scope = CoroutineScope(Dispatchers.IO)

    private val defaultClient =
        HttpClient(CIO) {
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
            }
            install(ContentNegotiation) {
                JsonUtils.customJson
            }
        }

    private var client = defaultClient

    fun setCustomHttpClient(client: HttpClient) {
        this.client = client
    }

    override suspend fun startRide(
        userId: Int,
        bikeId: String,
    ): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun getBikes(): List<EBike> =
        client.get(ServicesRoutes.EBIKE_ROUTE + "/all").let {
            JsonUtils.decodeHttpPayload<List<EBike>>(it)
                ?: listOf()
        }

    override suspend fun getBike(bikeId: String): EBike? =
        client.get("${ServicesRoutes.EBIKE_ROUTE}/$bikeId").let {
            JsonUtils.decodeHttpPayload<EBike>(it)
        }

    override suspend fun subscribeToSimulation(
        rideId: Int,
        rateIntervalMillis: Long,
    ): Flow<Ride> {
        simulations[rideId]?.let {
            logger.warn("Ride $rideId is already being monitored.")
            return it
        }

        val rideFlow = MutableSharedFlow<Ride>(replay = 1).also { simulations[rideId] = it }
        getRideSimulationStatusJob(rideId, rateIntervalMillis).also { jobs[rideId] = it }
        return rideFlow
    }

    override suspend fun stopRide(rideId: Int): Int {
        jobs.remove(rideId)?.cancel()
        simulations.remove(rideId)
        return rideId
    }

    override suspend fun stopAll() {
        jobs.values.forEach(Job::cancel)
        jobs.clear()
        simulations.clear()
    }

    private fun getRideSimulationStatusJob(
        rideId: Int,
        rate: Long,
    ) = scope.launch {
        client.webSocket("${ServicesRoutes.RIDE_SIM_ROUTE}/$rideId") {
            val senderJob =
                launch {
                    while (isActive) {
                        send(Frame.Text("status"))
                        delay(rate)
                    }
                }
            try {
                incoming.consumeEach {
                    val ride = receiveDeserialized<Ride>()
                    logger.debug("Got ride from websocket: {}", ride)
                    simulations[rideId]?.emit(ride)
                }
            } finally {
                senderJob.cancelAndJoin()
                close(CloseReason(CloseReason.Codes.NORMAL, "Stopped monitoring $rideId"))
            }
        }
    }
}
