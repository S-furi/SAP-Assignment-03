package it.unibo.sap.ass02.service.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingCall
import io.ktor.server.routing.delete
import io.ktor.server.routing.post
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import it.unibo.sap.ass02.domain.Ride
import it.unibo.sap.ass02.service.routing.Routes.CREATE_SIMULATION
import it.unibo.sap.ass02.service.routing.Routes.DELETE_SIMULATION
import it.unibo.sap.ass02.service.routing.Routes.SUBSCRIBE_TO_SIMULATION
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object RideSimulationRouting {
    private val messageResponseFlow = MutableSharedFlow<String>()
    private val sharedFlow = messageResponseFlow.asSharedFlow()
    private val logger: Logger = LoggerFactory.getLogger(RideSimulationRouting::class.java)

    fun Route.rideSimulation() {
        post(CREATE_SIMULATION) {
            val userId = call.queryParameters["userId"]?.toInt()
            val bikeId = call.queryParameters["bikeId"]
            if (userId == null || bikeId == null) {
                call.respond(HttpStatusCode.BadRequest, "An error occurred with provided ids: (bikeId: $bikeId, userId: $userId")
                return@post
            }
            RideSimulationResolver.createRide(bikeId, userId)?.let {
                call.respond(it)
            } ?: call.respond(HttpStatusCode.InternalServerError, "Something went wrong during ride creation...")
        }

        delete(DELETE_SIMULATION) {
            manipulateSimulation(call, RideSimulationResolver::deleteRide, { it }, "An error occurred during deletion...")
        }

        webSocket(SUBSCRIBE_TO_SIMULATION) {
            incoming.consumeEach { frame ->
                val id = call.parameters["id"]?.toInt() ?: return@consumeEach
                val command = ((frame as Frame.Text).readText().toRideCommand())
                when (command) {
                    RideCommand.START -> {
                        RideSimulationResolver.startRide(id)
                    }
                    RideCommand.STOP -> {
                        RideSimulationResolver.stopRide(id)
                    }
                    else -> {}
                }
                RideSimulationResolver.findRide(id)?.let {
                    val res = it.toJson().toString()
                    messageResponseFlow.emit(res)
                    logger.info("Got $command for ride id $id")
                    logger.info("Sending $res")
                }
            }
        }
    }

    private suspend fun manipulateSimulation(
        call: RoutingCall,
        op: suspend (Int) -> Any?,
        transformResponse: (Any) -> Any,
        errorMsg: String,
    ) {
        val id = extractRideIdFromRequest(call) ?: return
        op(id)?.let {
            call.respond(HttpStatusCode.OK, transformResponse(it))
        } ?: call.respond(HttpStatusCode.InternalServerError, errorMsg)
    }

    private suspend fun extractRideIdFromRequest(
        call: RoutingCall,
        idKey: String = "id",
    ): Int? {
        val id = call.parameters[idKey]?.toInt()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Provided rideId is null.")
        }
        return id
    }
}

object Routes {
    private const val BASE_PATH = "api/ride-service"
    const val CREATE_SIMULATION = "$BASE_PATH/create"
    const val DELETE_SIMULATION = "$BASE_PATH/{id}"
    const val SUBSCRIBE_TO_SIMULATION = "$BASE_PATH/{id}"
}

enum class RideCommand(
    s: String,
) {
    START("start"),
    STOP("stop"),
}

fun String.toRideCommand() =
    if (this == "start") {
        RideCommand.START
    } else if (this == "stop") {
        RideCommand.STOP
    } else {
        null
    }

fun Ride.toJson() =
    JsonObject(
        mapOf(
            "rideId" to JsonPrimitive(this.id),
            "ebikeId" to JsonPrimitive(this.ebike.id),
            "userId" to JsonPrimitive(this.user.id),
            "endDate" to JsonPrimitive(this.endDate.toString()),
            "startedDate" to JsonPrimitive(this.startedDate.toString()),
        ),
    )
