package it.unibo.sap.ass02.service.api

import io.ktor.http.*
import kotlinx.serialization.json.Json
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import it.unibo.sap.ass02.domain.RideImpl
import it.unibo.sap.ass02.service.api.RideRoutes.ALL_RIDES
import it.unibo.sap.ass02.service.api.RideRoutes.CREATE_RIDE
import it.unibo.sap.ass02.service.api.RideRoutes.DELETE_RIDE
import it.unibo.sap.ass02.service.api.RideRoutes.RIDE_BY_ID
import it.unibo.sap.ass02.service.api.RideRoutes.UPDATE_RIDE

object RideRouting {
    fun Route.rideRouting() {
        get(ALL_RIDES) {
            call.respond(RideResolver.getAllRides())
        }

        get(RIDE_BY_ID) {
//            handleGetRideByID(call)
            handleClientRequest(
                parameter = {call.parameters["id"]?.toInt()},
                handler = {id -> RideResolver.getRideByID(id) },
                handleOK = {obj -> call.respond(HttpStatusCode.OK, obj)},
                errorQuery = { call.respond(HttpStatusCode.BadRequest, "The input ID does not exist.") },
                errorNotFound = {call.respond(HttpStatusCode.BadRequest, "The input parameter is null.")})
        }

        post(CREATE_RIDE) {
            val rideText = call.receiveText()
            runCatching {
                val inputRide = Json.decodeFromString<RideImpl>(rideText)
                if (RideResolver.addNewRide(inputRide) != null) {
                    call.respond(HttpStatusCode.Conflict, "This ride already exists")
                    return@post
                }else {
                    call.respond(HttpStatusCode.Created)
                }
            }.getOrNull()?:call.respond(HttpStatusCode.BadRequest, "Error during the Ride parsing.")
        }

        put(UPDATE_RIDE) {
            val rideText = call.receiveText()
            runCatching {
                val inputRide = Json.decodeFromString<RideImpl>(rideText)
                if (RideResolver.updateRide(inputRide) != null) {
                    call.respond(HttpStatusCode.OK)
                }else{
                    call.respond(HttpStatusCode.BadRequest, "The Input Ride does not exist")
                }
            }.getOrNull()?:call.respond(HttpStatusCode.BadRequest, "Error during the Ride parsing")
        }

        delete(DELETE_RIDE) {
            handleClientRequest(
                parameter = { call.parameters["id"]?.toInt() },
                handler = {id -> RideResolver.deleteRide(id) },
                handleOK = {obj -> call.respond(HttpStatusCode.OK, obj)},
                errorQuery = { call.respond(HttpStatusCode.BadRequest, "The input ID does not exist.") },
                errorNotFound = {call.respond(HttpStatusCode.BadRequest, "The input parameter is null")})
        }
    }
/*

    private suspend fun handleGetRideByID(call: RoutingCall) {
        val rideID = call.parameters["id"]?.toInt()
        val message = "Error: the input Ride id does not exist or It is null."
        if (rideID == null) {
            call.respond(HttpStatusCode.BadRequest, message)
            return
        }else {
            RideResolver.getRideByID(rideID)?.let {
                call.respond(HttpStatusCode.OK, it)
            } ?: call.respond(HttpStatusCode.NotFound, message)
        }
    }
*/

    private suspend fun <K, T>handleClientRequest(parameter: () -> K?,
                                                  handler: suspend (K) -> T?,
                                                  handleOK: suspend (T) -> Unit,
                                                  errorQuery: suspend () -> Unit,
                                                  errorNotFound: suspend () -> Unit) {
        parameter()?.let {p ->
            handler(p)?.let {obj ->
                handleOK(obj)
            }?:errorQuery()
        }?:errorNotFound()
    }
}
