package it.unibo.sap.ass02.service.api

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import it.unibo.sap.ass02.service.api.UsersRoutes.USERS
import it.unibo.sap.ass02.service.api.UsersRoutes.USER_BY_ID

object UserRouting {
    fun Route.userRouting() {
        get(USERS) {
            call.respond(UserResolver.getAllUsers())
        }

        get(USER_BY_ID) {
            val userId: String? = this.call.parameters["id"]
            if (userId == null) {
                call.respond(HttpStatusCode.BadRequest, "Please specify a valid userId")
                return@get
            }
            UserResolver.getUserById(userId.toInt())
                ?.let { call.respond(this) }
                ?: call.respond(HttpStatusCode.NotFound, "Specified id does not correspond to a user")
        }
    }
}