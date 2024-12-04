package it.unibo.sap.ass02.service.routing

import io.ktor.server.routing.Route
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import it.unibo.sap.ass02.domain.Ride
import it.unibo.sap.ass02.service.routing.Routes.SUBSCRIBE_TO_SIMULATION
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object RideSimulationRouting {
    private val logger: Logger = LoggerFactory.getLogger(RideSimulationRouting::class.java)

    fun Route.rideSimulation() {
        webSocket(SUBSCRIBE_TO_SIMULATION) {
            incoming.consumeEach { frame ->
                val id = call.parameters["id"]?.toInt() ?: return@consumeEach
                runCatching {
                    when ((frame as Frame.Text).readText().toRideCommand()) {
                        RideCommand.START -> {
                            RideSimulationResolver.startRide(id)?.let { send(it.toString().asTextFrame()) }
                                ?: send("Something went wrong...".asTextFrame()).also { close() }
                        }

                        RideCommand.STOP -> {
                            RideSimulationResolver.stopRide(id)?.let { send(it.toString().asTextFrame()) }
                                ?: send("Something went wrong...".asTextFrame())
                            close()
                        }

                        RideCommand.STATUS -> {
                            RideSimulationResolver.findRide(id)?.let {
                                val res = it.toJson()
                                send(res.asTextFrame())
                            } ?: send("Not Found".asTextFrame())
                        }

                        else -> {
                            logger.warn("cannot parse unknown command: ${frame.readText()}")
                            close()
                        }
                    }
                }.onFailure {
                    logger.error("An error occurred: ${it.message}")
                    logger.error(it.stackTraceToString())
                    close(CloseReason(CloseReason.Codes.INTERNAL_ERROR, "Something went wrong in the backend: ${it.message}"))
                }
            }
        }
    }
}

private fun String.asTextFrame(fin: Boolean = true) = Frame.Text(fin, this.toByteArray())

object Routes {
    private const val BASE_PATH = "/"
    const val SUBSCRIBE_TO_SIMULATION = "$BASE_PATH{id}"
}

enum class RideCommand(
    s: String,
) {
    START("start"),
    STOP("stop"),
    STATUS("status"),
}

fun String.toRideCommand() =
    if (this == "start") {
        RideCommand.START
    } else if (this == "stop") {
        RideCommand.STOP
    } else if (this == "status") {
        RideCommand.STATUS
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
    ).toString()
