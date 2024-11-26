package it.unibo.sap.ass02.infrastructure

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory

sealed class Proxy(
    val healthcheckUri: String,
    val gatewayHost: String = System.getenv("GATEWAY_HOST") ?: "localhost",
    val gatewayPort: String = System.getenv("GATEWAY_PORT") ?: "4001",
) {
    protected val logger: Logger = LoggerFactory.getLogger(Proxy::class.java)

    val client =
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }

    init {
        runBlocking {
            val url = "http://$gatewayHost:$gatewayPort/$healthcheckUri"
            val res = client.get(url)
            require(res.status.value in 200..299) {
                "Cannot establish connection to \"$url\""
            }
        }
    }
}
