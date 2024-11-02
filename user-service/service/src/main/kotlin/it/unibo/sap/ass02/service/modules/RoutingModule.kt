package it.unibo.sap.ass02.service.modules

import io.ktor.server.application.Application
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.routing
import it.unibo.sap.ass02.service.api.UserRouting.userRouting

fun Application.routes() {
    routing {
        swaggerUI("docs", swaggerFile = "openapi/documentation.yaml")
        userRouting()
    }
}