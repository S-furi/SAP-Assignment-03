package it.unibo.sap.ass02.modules

import io.ktor.server.application.*
import io.ktor.server.routing.*
import it.unibo.sap.ass02.api.ApiGateway.apiGateway

fun Application.routes() {
    routing {
        apiGateway()
    }
}
