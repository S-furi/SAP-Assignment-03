package it.unibo.sap.ass02.service.modules

import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText

fun Application.installModule() {
    install(ContentNegotiation) {
        json()
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "Error: ${cause.message}", status = HttpStatusCode.InternalServerError)
        }
    }

    install(CORS) {
        anyHost()
        allowCredentials = true
        allowNonSimpleContentTypes = true
        headers {
            append("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE")
            append("Access-Control-Allow-Headers", "Content-Type, Authorization")
        }
    }
}