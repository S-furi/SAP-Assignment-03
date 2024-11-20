package it.unibo.sap.ass02.service.api

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import it.unibo.sap.ass02.domain.RideImpl
import it.unibo.sap.ass02.service.api.RideRoutes.ALL_RIDES
import it.unibo.sap.ass02.service.api.RideRoutes.CREATE_RIDE
import it.unibo.sap.ass02.service.api.RideRoutes.DELETE_RIDE
import it.unibo.sap.ass02.service.api.RideRoutes.RIDE_BY_ID
import it.unibo.sap.ass02.service.api.RideRoutes.UPDATE_RIDE
import kotlinx.serialization.json.Json

object RideRouting {
    fun Route.rideRouting() {
        get(ALL_RIDES) {
            call.respond(RideResolver.getAllRides())
        }

        get(RIDE_BY_ID) {
            handleClientRequest(
                parameter = call.parameters["id"]?.toInt(),
                handler = { id -> RideResolver.getRideByID(id) },
                handleOK = { obj -> call.respond(HttpStatusCode.OK, obj) },
                errorQuery = { call.respond(HttpStatusCode.BadRequest, "The input ID does not exist.") },
                errorNotFound = { call.respond(HttpStatusCode.BadRequest, "The input parameter is null.") },
            )
        }

        post(CREATE_RIDE) {
            val ebikeId = call.queryParameters["ebikeId"]
            val userId = call.queryParameters["userId"]?.toInt()

            if (ebikeId == null || userId == null) {
                call.respond(HttpStatusCode.BadRequest, "Error with provided ids: {ebike: $ebikeId, user: $userId}")
                return@post
            }
            RideResolver.addNewRide(ebikeId, userId)?.let {
                call.respond(HttpStatusCode.Created, it)
            } ?: call.respond(HttpStatusCode.Conflict, "This ride already exists")
        }

        put(UPDATE_RIDE) {
            val rideText = call.receiveText()
            runCatching {
                val inputRide = Json.decodeFromString<RideImpl>(rideText)
                RideResolver.updateRide(inputRide)?.let {
                    call.respond(HttpStatusCode.OK, it)
                } ?: call.respond(HttpStatusCode.BadRequest, "The Input Ride does not exist")
            }.getOrNull() ?: call.respond(HttpStatusCode.BadRequest, "Error during the Ride parsing")
        }

        delete(DELETE_RIDE) {
            handleClientRequest(
                parameter = call.parameters["id"]?.toInt(),
                handler = { id -> RideResolver.deleteRide(id) },
                handleOK = { obj -> call.respond(HttpStatusCode.OK, obj) },
                errorQuery = { call.respond(HttpStatusCode.BadRequest, "The input ID does not exist.") },
                errorNotFound = { call.respond(HttpStatusCode.BadRequest, "The input parameter is null") },
            )
        }
    }

    private suspend fun <K, T> handleClientRequest(
        parameter: K?,
        handler: suspend (K) -> T?,
        handleOK: suspend (T) -> Unit,
        errorQuery: suspend () -> Unit,
        errorNotFound: suspend () -> Unit,
    ) {
        parameter?.let { p ->
            handler(p)?.let { obj ->
                handleOK(obj)
            } ?: errorQuery()
        } ?: errorNotFound()
    }
}
