package it.unibo.sap.ass02.service.modules

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS

fun Application.installModule() {
    install(CORS) {
        anyHost()
    }

    install(ContentNegotiation) {
        json()
    }
}
