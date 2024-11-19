package it.unibo.sap.ass02.modules

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*

fun Application.installModule() {
    install(CORS) {
        anyHost()
        allowCredentials = true
        allowNonSimpleContentTypes = true
        headers {
            append("Access-Control-Allow-Methods", "GET, POST")
            append("Access-Control-Allow-Headers", "Content-Type, Authorization")
        }
    }
    install(ContentNegotiation) {
        json()
    }

}
