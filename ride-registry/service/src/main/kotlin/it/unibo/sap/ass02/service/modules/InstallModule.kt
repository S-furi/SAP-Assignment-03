package it.unibo.sap.ass02.service.modules

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry

fun Application.installModule() {
    install(CORS) {
        anyHost()
    }

    install(ContentNegotiation) {
        json()
    }
}

fun Application.metrics() {
    val registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

    install(MicrometerMetrics) {
        this.registry = registry
    }

    routing {
        get("/metrics") {
            call.respond(registry.scrape())
        }
    }
}
