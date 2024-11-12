package it.unibo.sap.ass02.domain.model.stub

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

sealed class Stub {
    val GATEWAY_HOST = System.getenv("GATEWAY_HOST") ?: "localhost"
    val GATEWAY_PORT = System.getenv("GATEWAY_PORT") ?: 1926
    val client = HttpClient(CIO)
}
