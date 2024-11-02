package it.unibo.sap.ass02.service.api

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingCall
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import it.unibo.sap.ass02.domain.User
import it.unibo.sap.ass02.service.api.UsersRoutes.USERS
import it.unibo.sap.ass02.service.api.UsersRoutes.USER_ADD
import it.unibo.sap.ass02.service.api.UsersRoutes.USER_BY_ID
import it.unibo.sap.ass02.service.api.UsersRoutes.USER_DECREASE_CREDIT
import it.unibo.sap.ass02.service.api.UsersRoutes.USER_DELETE
import it.unibo.sap.ass02.service.api.UsersRoutes.USER_INCREASE_CREDIT
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object UserRouting {
    fun Route.userRouting() {
        get(USERS) {
            call.respond(UserResolver.getAllUsers())
        }

        get(USER_BY_ID) {
            userIdFunc(this.call) { userId ->
                UserResolver
                    .getUserById(userId)
                    ?.let { call.respond(it) }
                    ?: call.respond(HttpStatusCode.NotFound, "Specified id does not correspond to a user")
            }
        }

        post(USER_ADD) {
            val request = call.receiveText()
            runCatching {
                val user: User = Json.decodeFromString<User>(request)
                if (UserResolver.createUser(user) != user.id) {
                    call.respond(HttpStatusCode.BadRequest, "The provided userId (${user.id}) is already present!")
                    return@post
                }
                call.respond(HttpStatusCode.OK)
            }.getOrNull()
                ?: call.respond(HttpStatusCode.BadRequest, "Error parsing request body: $request")
        }

        delete(USER_DELETE) {
            userIdFunc(this.call) { userId ->
                if (UserResolver.deleteUser(userId) == null) {
                    this.call.respond(HttpStatusCode.BadRequest, "Provided userId cannot be deleted.")
                } else {
                    this.call.respond(HttpStatusCode.OK)
                }
            }
        }

        post(USER_INCREASE_CREDIT) {
            manipulateCredit(this.call, UserResolver::increaseCredit)
        }

        post(USER_DECREASE_CREDIT) {
            manipulateCredit(this.call, UserResolver::decreaseCredit)
        }
    }

    private suspend fun userIdFunc(
        call: RoutingCall,
        errMsg: String = "Please specify a valid userId",
        ifPresent: suspend (Int) -> Unit,
    ) {
        val userId = call.parameters["id"]?.toInt()
        if (userId == null) {
            call.respond(HttpStatusCode.BadRequest, errMsg)
            return
        }
        ifPresent(userId)
    }

    private suspend fun manipulateCredit(
        call: RoutingCall,
        func: suspend (Int, Int) -> Boolean?,
    ) {
        val request = call.receiveText()
        runCatching {
            val creditOp = Json.decodeFromString<IncomingUserCreditOp>(request)
            val res = func(creditOp.id, creditOp.amount)
            if (res != null && res) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.BadRequest, "The provided id does not match to any user")
            }
        }.getOrElse {
            call.respond(HttpStatusCode.BadRequest, "An error occurred parsing request body: $request")
        }
    }

    @Serializable
    private data class IncomingUserCreditOp(
        val id: Int,
        val amount: Int,
    )
}
